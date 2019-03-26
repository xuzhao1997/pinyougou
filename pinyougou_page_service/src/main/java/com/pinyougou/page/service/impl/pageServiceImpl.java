package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.pageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/26$ 下午 04:12$
 */
@Service
@Transactional
public class pageServiceImpl implements pageService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;


   /**
   * @Description: 组装静态页生成所需要的参数
   * @Author:      XuZhao
   * @CreateDate:  19/03/26 下午 04:13
   */
    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();
        //tb_goods相关数据
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        goods.setGoods(tbGoods);
        //tb_goods_desc相关数据
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setGoodsDesc(tbGoodsDesc);
        //tb_item列表相关数据
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);
        return goods;
    }
}
