package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillGoods;

import java.util.List;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 09:47$
 */
public interface SeckillService {

    /**
     * @Description: 查询秒杀商品列表
     * @Author:      XuZhao
     * @CreateDate:  19/04/06 下午 09:35
     */
    public List<TbSeckillGoods> selectSeckillGoodsFromRedis();

}
