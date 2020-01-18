package com.imooc.mall.dao;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.pojo.PayInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PayInfoMapperTest extends MallApplicationTests {

    @Autowired
    PayInfoMapper payInfoMapper;

    @Test
    public void selectByPrimaryKey() {
        PayInfo payInfo = payInfoMapper.selectByPrimaryKey(1);
        // System.out.println(payInfo.toString());
    }
}