package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;

/**
 * @ClassName: SpecificationService
 * @Description: 商品规格接口
 * @Author: XuZhao
 * @CreateDate: 19/03/12$ 下午 07:16$
 */
public interface SpecificationService {
    //分页查询所有规格
    PageResult search(TbSpecification specification, Integer pageNum, Integer pageSize);
}
