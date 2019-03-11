package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PageResult
 * @Description: 分页封装类
 * @Author: XuZhao
 * @CreateDate: 19/03/11$ 下午 03:33$
 */
public class PageResult implements Serializable {

    private Long total;
    private List rows;

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
