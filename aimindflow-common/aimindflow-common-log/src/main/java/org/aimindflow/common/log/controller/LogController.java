package org.aimindflow.common.log.controller;

import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.log.annotation.Log;
import org.aimindflow.common.log.constant.LogConstants;
import org.aimindflow.common.log.entity.LogInfo;
import org.aimindflow.common.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 日志控制器
 *
 * @author HezaoHezao
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 记录日志
     *
     * @param logInfo 日志信息
     * @return 结果
     */
    @PostMapping("/record")
    @Log(module = "日志管理", operationType = LogConstants.OPERATION_TYPE_INSERT, description = "记录日志")
    public R<Void> recordLog(@RequestBody LogInfo logInfo) {
        logService.log(logInfo);
        return R.ok();
    }

    /**
     * 记录成功日志
     *
     * @param module      模块名称
     * @param description 操作描述
     * @return 结果
     */
    @PostMapping("/success")
    @Log(module = "日志管理", operationType = LogConstants.OPERATION_TYPE_INSERT, description = "记录成功日志")
    public R<Void> recordSuccessLog(@RequestParam String module, @RequestParam String description) {
        logService.logSuccess(module, LogConstants.OPERATION_TYPE_INSERT, description);
        return R.ok();
    }

    /**
     * 记录失败日志
     *
     * @param module      模块名称
     * @param description 操作描述
     * @param errorMsg    错误消息
     * @return 结果
     */
    @PostMapping("/fail")
    @Log(module = "日志管理", operationType = LogConstants.OPERATION_TYPE_INSERT, description = "记录失败日志")
    public R<Void> recordFailLog(@RequestParam String module, @RequestParam String description, @RequestParam String errorMsg) {
        logService.logFail(module, LogConstants.OPERATION_TYPE_INSERT, description, new RuntimeException(errorMsg));
        return R.ok();
    }

    /**
     * 记录异常日志
     *
     * @param module      模块名称
     * @param description 操作描述
     * @param errorMsg    错误消息
     * @return 结果
     */
    @PostMapping("/error")
    @Log(module = "日志管理", operationType = LogConstants.OPERATION_TYPE_INSERT, description = "记录异常日志")
    public R<Void> recordErrorLog(@RequestParam String module, @RequestParam String description, @RequestParam String errorMsg) {
        logService.logException(module, description, new RuntimeException(errorMsg));
        return R.ok();
    }

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @return 结果
     */
    @PostMapping("/login")
    public R<Void> recordLoginLog(@RequestParam String username, @RequestParam String status, @RequestParam String message) {
        logService.logLogin(username, status, message);
        return R.ok();
    }

    /**
     * 记录登出日志
     *
     * @param username 用户名
     * @return 结果
     */
    @PostMapping("/logout")
    public R<Void> recordLogoutLog(@RequestParam String username) {
        logService.logLogout(username);
        return R.ok();
    }

    /**
     * 测试日志
     *
     * @return 结果
     */
    @GetMapping("/test")
    @Log(module = "日志管理", operationType = LogConstants.OPERATION_TYPE_QUERY, description = "测试日志")
    public R<String> testLog() {
        return R.ok("测试日志成功");
    }
}