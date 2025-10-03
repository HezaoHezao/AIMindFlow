package org.aimindflow.common.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.aimindflow.common.core.model.PageResult;

import java.util.List;

/**
 * 分页工具类
 *
 * @author HezaoHezao
 */
public class PageUtils {

    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页条数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页条数
     */
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * 开始分页
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     */
    public static void startPage(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 获取分页结果
     *
     * @param list 列表数据
     * @param <T>  泛型
     * @return 分页结果
     */
    public static <T> PageResult<T> getPageResult(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setList(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }

    /**
     * 创建MyBatis-Plus分页对象
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param <T>      泛型
     * @return 分页对象
     */
    public static <T> Page<T> createPage(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        return new Page<>(pageNum, pageSize);
    }

    /**
     * 将MyBatis-Plus分页结果转换为通用分页结果
     *
     * @param page MyBatis-Plus分页结果
     * @param <T>  泛型
     * @return 通用分页结果
     */
    public static <T> PageResult<T> convertPageResult(IPage<T> page) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setList(page.getRecords());
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNum((int) page.getCurrent());
        pageResult.setPageSize((int) page.getSize());
        pageResult.setPages((int) page.getPages());
        return pageResult;
    }
}