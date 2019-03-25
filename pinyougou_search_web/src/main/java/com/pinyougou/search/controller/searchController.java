package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: SearchController
 * @Description: 商品搜索表现层
 * @Author: XuZhao
 * @CreateDate: 19/03/25$ 下午 04:42$
 */
@RestController
@RequestMapping("/search")
public class searchController {

    @Reference(timeout = 30000)
    private SearchService searchService;


    /**
    * @Description: 商品搜索功能
    * @Author:      XuZhao
    * @CreateDate:  19/03/25 下午 04:44
    */
    @RequestMapping("/searchItem")
    public Map<String,Object> searchItem(@RequestBody Map searchMap){
        return searchService.search(searchMap);
    }

}
