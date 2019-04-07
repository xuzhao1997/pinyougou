package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
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

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;


    /**
    * @Description: 查询秒杀商品列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 09:39
    */
    @Override
    public List<TbSeckillGoods> selectSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }

    /**
    * @Description: 基于秒杀商品id查询秒杀商品详情
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 04:05
    */
    @Override
    public TbSeckillGoods findOne(Long seckillGoodsId) {
        return (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
    }

    /**
    * @Description: 保存秒杀订单
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 05:02
    */
    @Override
    public void submitSeckillOrder(Long seckillGoodsId,String userId) {
        //获取秒杀商品
        TbSeckillGoods seckillGoods =  (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
        //判断是否还有商品
        if(seckillGoods == null || seckillGoods.getStockCount() <= 0){
            throw  new RuntimeException("不好意思,您手慢了,商品已经售罄了");
        }
        //生成秒杀订单
        TbSeckillOrder seckillOrder = new TbSeckillOrder();
        //id
        long seckillOrderId = idWorker.nextId();
        seckillOrder.setId(seckillOrderId);
        //金额
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        //user_id
        seckillOrder.setUserId(userId);
        //seller_id
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        //创建时间
        seckillOrder.setCreateTime(new Date());
        //状态
        seckillOrder.setStatus("1");//1未支付

        //秒杀下单保存到数据库
        seckillOrderMapper.insert(seckillOrder);
        //秒杀下单,要将redis中秒杀商品库存减1
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        //同步数据库的时机,当库存为0或者秒杀结束
        if(seckillGoods.getStockCount()==0 || (new Date().getTime() > seckillGoods.getEndTime().getTime())){
            //更新秒杀商品库存到数据库
            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
            //清除redis中秒杀商品
            redisTemplate.boundHashOps("seckill_goods").delete(seckillGoodsId);
        }else{
            //秒杀下单,秒杀还没有结束,此时要更新redis缓存中秒杀商品库存
            redisTemplate.boundHashOps("seckill_goods").put(seckillGoodsId,seckillGoods);
        }

    }
}
