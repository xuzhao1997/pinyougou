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
}
