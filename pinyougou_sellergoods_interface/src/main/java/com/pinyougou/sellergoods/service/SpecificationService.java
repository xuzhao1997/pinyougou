package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;
import groupEntity.Specification;

/**
 * @ClassName: SpecificationService
 * @Description: 商品规格接口
 * @Author: XuZhao
 * @CreateDate: 19/03/12$ 下午 07:16$
 */
public interface SpecificationService {
    //分页查询所有规格
    PageResult search(TbSpecification specification, Integer pageNum, Integer pageSize);

    //新增规格
    void insert(Specification specification);

    //修改操作的数据回显
    Specification findOne(Long id);

    //修改规格
    void update(Specification specification);

    //删除规格
    void delete(Long[] ids);
}
