package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
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
        try {
            //获取支付订单号,和支付金额,
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
           TbPayLog payLog =  payService.getPayLog(userId);
           return payService.createNative(payLog.getOutTradeNo(),payLog.getTotalFee()+"");
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
    * @Description: 查询支付状态
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 04:44
    */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        try {
            int count=1;
            while (true){
                //每隔三秒调一次
                Thread.sleep(3000);

                //跳出循环的时机,设置二维码失效.
                count++;
                if(count>100){
                    return new Result(false,"timeout");
                }

                //查询结果集合参数
                Map<String, String> resultMap = payService.queryPayStatus(out_trade_no);
                //获取支付状态
                String trade_state = resultMap.get("trade_state");
                if("SUCCESS".equals(trade_state)){
                    //支付成功,更新订单状态和支付日志状态
                    String transaction_id = resultMap.get("transaction_id");//微信交易流水号
                    payService.updatePayStatus(out_trade_no,transaction_id);
                    return new Result(true,"支付成功!!!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"支付失败!!!");
        }
    }


}
