package com.imooc.pay.service;

import java.math.BigDecimal;

public interface IPayService {

    /**
     * 創建發起支付
     * @param orderId
     * @param amount
     */
    void create (String orderId, BigDecimal amount);
}
