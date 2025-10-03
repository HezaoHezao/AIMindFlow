package org.aimindflow.common.idempotent.controller;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.idempotent.constant.IdempotentConstants;
import org.aimindflow.common.idempotent.util.IdempotentUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 幂等控制器
 *
 * @author HezaoHezao
 */
@Slf4j
@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

    /**
     * 获取幂等Token
     *
     * @param businessKey 业务Key
     * @return 幂等Token
     */
    @GetMapping("/token/{businessKey}")
    public R<String> getToken(@PathVariable String businessKey) {
        // 生成幂等Token
        String token = businessKey + ":" + System.currentTimeMillis();
        // 设置幂等标记
        boolean result = IdempotentUtils.setIdempotentMark(token, IdempotentConstants.DEFAULT_EXPIRE_TIME);
        if (result) {
            return R.ok(token);
        } else {
            return R.fail("获取幂等Token失败");
        }
    }

    /**
     * 检查幂等Token是否有效
     *
     * @param token 幂等Token
     * @return 是否有效
     */
    @GetMapping("/check/{token}")
    public R<Boolean> checkToken(@PathVariable String token) {
        boolean result = IdempotentUtils.checkExecutable(token, IdempotentConstants.DEFAULT_EXPIRE_TIME);
        return R.ok(result);
    }

    /**
     * 删除幂等Token
     *
     * @param token 幂等Token
     * @return 是否成功
     */
    @DeleteMapping("/token/{token}")
    public R<Boolean> deleteToken(@PathVariable String token) {
        boolean result = IdempotentUtils.removeIdempotentMark(token);
        return R.ok(result);
    }

    /**
     * 幂等测试接口
     *
     * @param token 幂等Token
     * @return 测试结果
     */
    @PostMapping("/test/{token}")
    public R<String> test(@PathVariable String token) {
        try {
            // 执行幂等操作
            return IdempotentUtils.execute(token, IdempotentConstants.DEFAULT_EXPIRE_TIME, () -> {
                // 模拟业务处理
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return R.ok("幂等测试成功，Token: " + token);
            });
        } catch (Exception e) {
            log.error("幂等测试异常", e);
            return R.fail(e.getMessage());
        }
    }
}