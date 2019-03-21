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

        //启用规格
        if(tbGoods.getIsEnableSpec().equals("1")){//启用规格
            //tb_item表数据保存
            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
//		  `title` varchar(100) NOT NULL COMMENT '商品标题',   // 商品名称（SPU名称）+ 商品规格选项名称 中间以空格隔开
                String title=tbGoods.getGoodsName();
                //获取商品规格选项名称
                //:{"网络":"移动3G","机身内存":"32G"}
                String spec = item.getSpec();
                Map<String,String> map = JSON.parseObject(spec, Map.class);
                for(String key : map.keySet()){
                    title+=" "+ map.get(key);
                }
                item.setTitle(title);
                //封装tb_item后端需要组装的数据
                setItemValue(tbGoods, goodsDesc, item);

                itemMapper.insert(item);
            }
        }else {//没有启用规格
            //需要后端自己组装sku（item）的相关数据
            TbItem item = new TbItem();
            //spu名称作为title
            String title=tbGoods.getGoodsName();
            item.setTitle(title);

            //封装tb_item后端需要组装的数据
            setItemValue(tbGoods, goodsDesc, item);

            //封装原本由页面提交的数据
            item.setSpec("{}");
            item.setPrice(tbGoods.getPrice());
            item.setNum(999);
            item.setStatus("1");
            item.setIsDefault("1");
            itemMapper.insert(item);
        }


    }

    /**
    * @Description: 封装tb_item表后端需要组装的数据
    * @Author:      XuZhao
    * @CreateDate:  19/03/20 下午 07:43
    */
    private void setItemValue(TbGoods tbGoods, TbGoodsDesc goodsDesc, TbItem item) {
        //		  `image` varchar(2000) DEFAULT NULL COMMENT '商品图片',  // 从 tb_goods_desc item_images中获取第一张
        //[{"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVnGbYuAO6AHAAjlKdWCzvg253.jpg"},
        // {"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVnKX5WAOsqXAAETwD7A1Is409.jpg"}]
        String itemImages = goodsDesc.getItemImages();
        List<Map> imageList = JSON.parseArray(itemImages, Map.class);
        if (imageList!=null && imageList.size()>0) {
            String image= (String) imageList.get(0).get("url");
            item.setImage(image);
        }
//		  `categoryId` bigint(10) NOT NULL COMMENT '所属类目，叶子类目',  //三级分类id
        item.setCategoryid(tbGoods.getCategory3Id());
//		  `create_time` datetime NOT NULL COMMENT '创建时间',
        item.setCreateTime(new Date());
//		  `update_time` datetime NOT NULL COMMENT '更新时间',
        item.setUpdateTime(new Date());
//		  `goods_id` bigint(20) DEFAULT NULL,
        item.setGoodsId(tbGoods.getId());
//		  `seller_id` varchar(30) DEFAULT NULL,
        item.setSellerId(tbGoods.getSellerId());
//				//以下字段作用：方便商品搜索
//		  `category` varchar(200) DEFAULT NULL, //三级分类名称
        String category = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
        item.setCategory(category);
//		  `brand` varchar(100) DEFAULT NULL,//品牌名称
        String brandName = brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName();
        item.setBrand(brandName);
//		  `seller` varchar(200) DEFAULT NULL,//商家店铺名称
        String nickName = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getNickName();
        item.setSeller(nickName);
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
		//删除只是修改状态让运营商看不到
		for(Long id:ids){
			/*goodsMapper.deleteByPrimaryKey(id);*/
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);
		}
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//非删除状态，让查询的商品都是没删除的
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				/*criteria.andSellerIdLike("%"+goods.getSellerId()+"%");*/
                            criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				/*criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");*/
                criteria.andAuditStatusEqualTo(goods.getAuditStatus());
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

	/**
	* @Description: 商品审核
	* @Author:      XuZhao
	* @CreateDate:  19/03/20 下午 09:22
	*/
    @Override
    public void updateStatus(Long[] ids, String status) {
        //商品审核
        for(Long id : ids){
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
    * @Description: 商品上下架
    * @Author:      XuZhao
    * @CreateDate:  19/03/20 下午 09:46
    */
    @Override
    public void updateIsMarketable(Long[] ids, String isMarketable) {
        for(Long id : ids){
            //判断只有审核通过的商品才能上架
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            if(tbGoods.getAuditStatus().equals("1")){
                tbGoods.setIsMarketable(isMarketable);
                goodsMapper.updateByPrimaryKey(tbGoods);
            }else{
                throw new RuntimeException("只有审核通过的商品才能上架");
            }

        }

    }

}
