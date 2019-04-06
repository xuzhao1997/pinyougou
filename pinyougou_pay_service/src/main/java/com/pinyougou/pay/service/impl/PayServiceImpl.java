package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.PayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PayServiceImpl
 * @Description: 微信支付实现类
 * @Author: XuZhao
 * @CreateDate: 19/04/04$ 下午 04:25$
 */
@Service
@Transactional
public class PayServiceImpl implements PayService {

    //公众号id
    @Value("${appid}")
    private String appid;

    //商户号id
    @Value("${partner}")
    private String partner;

    //商户密钥
    @Value("${partnerkey}")
    private String partnerkey;

    //回调地址
    @Value("${notifyurl}")
    private String notifyurl;


    /**
    * @Description: 生成二维码
    * @Author:      XuZhao
    * @CreateDate:  19/04/04 下午 04:26
    */
    @Override
    public Map<String, Object> createNative(String out_trade_no, String total_fee) throws Exception {
        //1.组装调用统一下单接口所必须准备的参数
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("mch_id",partner);
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
        paramMap.put("body","品优购");
        paramMap.put("out_trade_no",out_trade_no);
        paramMap.put("total_fee",total_fee);
        paramMap.put("spbill_create_ip","127.0.0.1");
        paramMap.put("notify_url",notifyurl);
        paramMap.put("trade_type","NATIVE");
        paramMap.put("product_id","1");

        //2.调用微信支付平台统一下单接口,获取支付超链接
        String paramXML = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        System.out.println(paramXML);

        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXML);
        httpClient.post();

        //3.处理支付平台返回的响应结果
        String resultXml = httpClient.getContent();
        System.out.println(resultXml);
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        String code_url = resultMap.get("code_url");

        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no",out_trade_no);
        map.put("total_fee",total_fee);
        map.put("code_url",code_url);
        return map;
    }


    /**
    * @Description: 查询微信支付状态
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 04:34
    */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) throws Exception {
        //组装调用查询支付状态接口所必须准备的参数
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("mch_id",partner);
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
        paramMap.put("out_trade_no",out_trade_no);
        //调用查询微信支付状态
        String paramxml = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        System.out.println(paramxml);
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramxml);
        httpClient.post();

        //处理支付平台返回的响应结果
        String resultXml = httpClient.getContent();
        System.out.println(resultXml);
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

        return resultMap;
    }
}
