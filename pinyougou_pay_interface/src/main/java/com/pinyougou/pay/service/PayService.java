package com.pinyougou.pay.service;

import java.util.Map;

/**
 * @ClassName: PayService
 * @Description: 支付接口
 * @Author: XuZhao
 * @CreateDate: 19/04/04$ 下午 04:08$
 */
public interface PayService {


    /**
    * @Description: 生成二维码
    * @Author:      XuZhao
    * @CreateDate:  19/04/04 下午 04:08
    */
    public Map<String,Object> createNative(String out_trade_no,String total_fee) throws Exception;

    /**
    * @Description: 查询微信支付状态
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 04:32
    */

    public Map<String,String> queryPayStatus(String out_trade_no) throws Exception;
}
