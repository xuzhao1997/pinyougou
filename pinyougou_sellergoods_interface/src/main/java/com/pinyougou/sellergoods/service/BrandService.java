package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @ClassName: BrandService
 * @Description: 查询品牌列表
 * @Author: XuZhao
 * @CreateDate: 19/03/09$ 下午 07:41$
 */
public interface BrandService {
    //查询品牌列表
    public List<TbBrand> findAll();

    //分页查询品牌列表
    PageResult findPage(Integer pageNum, Integer pageSize);
}
