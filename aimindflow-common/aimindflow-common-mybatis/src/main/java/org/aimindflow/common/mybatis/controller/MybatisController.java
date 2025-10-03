package org.aimindflow.common.mybatis.controller;

import org.aimindflow.common.core.model.PageResult;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.mybatis.annotation.DataScope;
import org.aimindflow.common.mybatis.annotation.DataSource;
import org.aimindflow.common.mybatis.utils.PageUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis控制器
 *
 * @author HezaoHezao
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController {

    /**
     * 测试分页
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping("/page")
    public R<PageResult<Map<String, Object>>> testPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // 模拟分页数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i + 1);
            map.put("name", "测试" + (i + 1));
            list.add(map);
        }

        // 手动分页
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        List<Map<String, Object>> pageList = list.subList(start, end);

        // 构建分页结果
        PageResult<Map<String, Object>> pageResult = new PageResult<>();
        pageResult.setList(pageList);
        pageResult.setTotal(list.size());
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setPages((int) Math.ceil((double) list.size() / pageSize));

        return R.ok(pageResult);
    }

    /**
     * 测试数据权限
     *
     * @return 结果
     */
    @GetMapping("/dataScope")
    @DataScope(deptAlias = "d", userAlias = "u")
    public R<String> testDataScope() {
        return R.ok("测试数据权限成功");
    }

    /**
     * 测试主数据源
     *
     * @return 结果
     */
    @GetMapping("/master")
    @DataSource("master")
    public R<String> testMaster() {
        return R.ok("测试主数据源成功");
    }

    /**
     * 测试从数据源
     *
     * @return 结果
     */
    @GetMapping("/slave")
    @DataSource("slave")
    public R<String> testSlave() {
        return R.ok("测试从数据源成功");
    }
}