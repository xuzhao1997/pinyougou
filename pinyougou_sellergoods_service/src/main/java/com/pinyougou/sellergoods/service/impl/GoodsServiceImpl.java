package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
    private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
    private TbBrandMapper brandMapper;

	@Autowired
    private TbSellerMapper sellerMapper;

	@Autowired
    private TbItemMapper itemMapper;

	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
        //tb_goods表数据保存
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0");//未审核状态
        goodsMapper.insert(tbGoods);

        //tb_goods_desc表数据保存
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();
        goodsDesc.setGoodsId(tbGoods.getId());
        goodsDescMapper.insert(goodsDesc);
		//tb_item表数据保存\
		List<TbItem> itemList = goods.getItemList();
		for (TbItem tbItem : itemList) {
			String title = tbGoods.getGoodsName();
			//获取商品规格选项名称
            String spec = tbItem.getSpec();
            Map<String,String> map = JSON.parseObject(spec, Map.class);
            for(String key:map.keySet()){
                title+=" "+map.get(key);
            }
            tbItem.setTitle(title);
            //取图片
            String itemImages = goodsDesc.getItemImages();
            List<Map> imagesList = JSON.parseArray(itemImages, Map.class);
            if(imagesList!=null && imagesList.size()>0){
                String image = (String) imagesList.get(0).get("url");
                tbItem.setImage(image);
            }
            //三级分类
            tbItem.setCategoryid(tbGoods.getCategory3Id());
            //创建时间
            tbItem.setCreateTime(new Date());
            //更新时间
            tbItem.setUpdateTime(new Date());
            //goods_id
            tbItem.setGoodsId(tbGoods.getId());
            //seller_id
            tbItem.setSellerId(tbGoods.getSellerId());
            //三级分类名称
            String category = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
            tbItem.setCategory(category);
            //品牌名称
            String brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName();
            tbItem.setBrand(brand);
            //商家店铺名称
            String seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getNickName();
            tbItem.setSeller(seller);
            itemMapper.insert(tbItem);
        }

    }

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
