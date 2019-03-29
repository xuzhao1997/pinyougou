package com.pinyougou.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.pinyougou.sms.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: smsController
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/29$ 下午 05:26$
 */
@RestController
@RequestMapping("/sms")
public class smsController {


    @Autowired
    private SmsUtil smsUtil;

    /**
    * @Description: 调用阿里大于发送短信
    * @Author:      XuZhao
    * @CreateDate:  19/03/29 下午 05:28
    */
    @RequestMapping(value = "/sendSms",method = RequestMethod.POST)
    public Map<String,String> sendSms(String phoneNumbers,String signName,String templateCode,String param){
        try {
            SendSmsResponse response = smsUtil.sendSms(phoneNumbers, signName, templateCode, param);

            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());

            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("Code",response.getCode());
            resultMap.put("Message",response.getMessage());
            resultMap.put("RequestId",response.getRequestId());
            resultMap.put("BizId",response.getBizId());

            return resultMap;
        } catch (ClientException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


}
