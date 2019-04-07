package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 09:38$
 */
@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
    * @Description: 查询秒杀商品列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 09:39
    */
    @Override
    public List<TbSeckillGoods> selectSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }
}
