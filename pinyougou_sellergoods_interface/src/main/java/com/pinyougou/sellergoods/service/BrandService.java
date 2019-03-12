package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

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

    //新增品牌
    void add(TbBrand tbBrand);

    //根据id查询
    TbBrand findOne(Long id);

    //修改品牌
    void update(TbBrand tbBrand);

    //批量删除
    void delete(Long[] ids);

    //品牌分页查询
    PageResult search(TbBrand brand, Integer pageNum, Integer pageSize);
}
