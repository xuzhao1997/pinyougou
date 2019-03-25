package com.pinyougou.search.service;

import java.util.Map;

/**
 * @ClassName: SearchService
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/25$ 下午 04:20$
 */
public interface SearchService {

    /**
    * @Description: 商品搜索
    * @Author:      XuZhao
    * @CreateDate:  19/03/25 下午 04:21
    */
    public Map<String,Object> search(Map searchMap);

}
