package com.imooc.pay.service.impl;

import com.imooc.pay.PayApplicationTests;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by 廖师兄
 */
public class PayServiceTest extends PayApplicationTests {

	@Autowired
	private PayService payService;

	@Test
	public void create() {
		//BigDecimal.valueOf(0.01)
		//new BigDecimal("0.01")  千万不要用 new BigDecimal(0.01)
		payService.create("1234567891234567", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
	}
}