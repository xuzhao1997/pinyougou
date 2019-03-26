package com.pinyougou.page.service;

import groupEntity.Goods;

/**
 * @ClassName: pageService
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/26$ 下午 04:08$
 */
public interface pageService {

    /**
    * @Description: 组装静态页生成所需要的参数
    * @Author:      XuZhao
    * @CreateDate:  19/03/26 下午 04:09
    */
    public Goods findOne(Long goodsId);

}
