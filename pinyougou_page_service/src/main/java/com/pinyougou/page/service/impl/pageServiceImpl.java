package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.pageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TbItemCatMapper itemCatMapper;


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
        //组装商品分类数据
        //一级分类id
        String category1Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
        //二级
        String category2Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
        //三级
        String category3Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
        //组装分类列表数据
        Map<String,String> categoryMap = new HashMap<>();
        categoryMap.put("category1Name",category1Name);
        categoryMap.put("category2Name",category2Name);
        categoryMap.put("category3Name",category3Name);
        goods.setCategoryMap(categoryMap);

        //tb_item列表相关数据
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);
        return goods;
    }
}
