package com.imooc.mall.service.impl;

import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.OrderStatusEnum;
import com.imooc.mall.enums.PaymentTypeEnum;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderItemVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    ShippingMapper shippingMapper;

    @Autowired
    ICartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        // 收貨地址校驗
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        // 獲取購物車，較驗(是否有商品、庫存)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        // 獲取 cart:ist 中的 productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            // 根據 productId 查詢數據庫
            Product product = map.get(cart.getProductId());
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "商品不存在. productId = " + cart.getProductId());
            }

            // 商品上下架狀態
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_OR_DELETE, "商品不是在售狀態." + product.getName());
            }

            // 庫存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, "庫存不正確. " + product.getName());
            }

            OrderItem orderItem = builderOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            // 減庫存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row < 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        // 計算總價，只計算選中的商品
        // 生成訂單、入庫: order & order_item 事務
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        // 更新購物車 (選中的商品)
        // Redis 有事務(打包命令)，不能回滾
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        // 構造 orderVo
        OrderVo orderVo = builderOrderVo(order, orderItemList, shipping);
        return ResponseVo.success();
    }

    private OrderVo builderOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e ->
        {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
     * 企業級: 分布是唯一id/主鍵
     *
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem builderOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
