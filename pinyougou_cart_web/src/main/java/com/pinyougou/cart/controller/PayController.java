package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.PayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PyController
 * @Description: 微信支付二维码生成
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 03:12$
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference(timeout = 5000)
    private PayService payService;


    /**
     * @Description: 生成二维码
     * @Author: XuZhao
     * @CreateDate: 19/04/06 下午 03:13
     */
    @RequestMapping("/createNative")
    public Map<String, Object> createNative() {
        //TODO 为了测试,基于假的支付订单号和支付金额,后面完成后再修改
        IdWorker idWorker = new IdWorker();
        try {
           return payService.createNative(idWorker.nextId()+"","1");
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

    }

}
