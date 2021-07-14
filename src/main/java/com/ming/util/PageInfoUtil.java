package com.ming.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Serializable
 *
 * @Author liming
 * @Date: 2021-06-25 11:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfoUtil<T> implements Serializable {

    private Integer pageSize = 1; // 每页显示多少条记录
    private Integer currentPage; //当前第几页数据
    private Integer total; //一共多少条记录
    private Integer totalPages; //总页数
    private Integer previousPage; //前一页
    private Integer nextPage;//后一页
    private List<T> list;//要分页的数组


    public PageInfoUtil(List<T> list, Integer pageNum, Integer pageSize) {
        if (list == null) {
            return;
        }
        //总记录条数
        this.total = list.size();
        //每页显示多少条记录
        this.pageSize = pageSize;
        //获取总页数
        //1/3 2/3 3/3 3页11条数据
        // 11%5+1===》2+1=3  如果总条数/每页显示多少条不等于0 总页数加1
        this.totalPages = this.total / this.pageSize;
        if (this.total % this.pageSize != 0) {
            this.totalPages = totalPages + 1;
        }

        //当前第几页数据
        this.currentPage = this.totalPages < pageNum ? this.totalPages : pageNum;
        //上一页
        this.previousPage = currentPage > 0 ? currentPage - 1 : currentPage;
        //下一页
        this.nextPage = currentPage < totalPages ? currentPage + 1 : currentPage;

        // 起始索引
        int fromIndex = this.pageSize * (this.currentPage - 1);
        // 结束索引
        int toIndex = this.pageSize * this.currentPage > this.total ? this.total : this.pageSize * this.currentPage;
        //不包含结束索引的值
        this.list = list.subList(fromIndex, toIndex);

    }


}
