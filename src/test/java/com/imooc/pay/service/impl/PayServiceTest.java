package com.imooc.pay.service.impl;

import com.imooc.pay.PayApplicationTests;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@SpringBootTest
public class PayServiceTest extends PayApplicationTests {
    @Autowired
    private PayService payService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void create(){
        //BigDecimal.valueOf(0.01) =  new BigDecimal("0.01")   不能用new BigDecimal(0.01)
       // payService.create("123456789", BigDecimal.valueOf(0.01), BestPayTypeEnum bestPayTypeEnum);
    }

    @Test
    public void sendAmqp(){
        amqpTemplate.convertAndSend("payNotify","hello");
    }
}