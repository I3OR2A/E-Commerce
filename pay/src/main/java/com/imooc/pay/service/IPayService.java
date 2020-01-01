package com.imooc.pay.service;

import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

public interface IPayService {

    /**
     * 創建發起支付
     * @param orderId
     * @param amount
     */
    PayResponse create (String orderId, BigDecimal amount);

    /**
     * 異步通知處理
     */
    String asyncNotify(String notifyData);
}
