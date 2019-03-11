package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @ClassName: TbBrandMapper
 * @Description: 品牌接口层
 * @Author: XuZhao
 * @CreateDate: 19/03/09$ 下午 07:35$
 */
public interface TbBrandMapper {
    //查询所有品牌列表
    List<TbBrand> findAll();

    //新增品牌
    public void add(TbBrand tbBrand);

    //根据id查询
    TbBrand selectByPrimaryKey(Long id);

    //品牌修改
    void updateByPrimaryKey(TbBrand tbBrand);

    //批量删除
    void deleteByPrimaryKey(Long id);
}
