package org.aimindflow.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字段自动填充处理器
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {

    /**
     * 创建者字段名
     */
    private static final String CREATE_BY = "createBy";

    /**
     * 创建时间字段名
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 更新者字段名
     */
    private static final String UPDATE_BY = "updateBy";

    /**
     * 更新时间字段名
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 删除标志字段名
     */
    private static final String DEL_FLAG = "delFlag";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 创建者
            if (metaObject.hasSetter(CREATE_BY)) {
                this.strictInsertFill(metaObject, CREATE_BY, String.class, getCurrentUsername());
            }
            // 创建时间
            if (metaObject.hasSetter(CREATE_TIME)) {
                this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
            }
            // 更新者
            if (metaObject.hasSetter(UPDATE_BY)) {
                this.strictInsertFill(metaObject, UPDATE_BY, String.class, getCurrentUsername());
            }
            // 更新时间
            if (metaObject.hasSetter(UPDATE_TIME)) {
                this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
            }
            // 删除标志
            if (metaObject.hasSetter(DEL_FLAG)) {
                this.strictInsertFill(metaObject, DEL_FLAG, Integer.class, 0);
            }
        } catch (Exception e) {
            log.error("字段自动填充异常", e);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            // 更新者
            if (metaObject.hasSetter(UPDATE_BY)) {
                this.strictUpdateFill(metaObject, UPDATE_BY, String.class, getCurrentUsername());
            }
            // 更新时间
            if (metaObject.hasSetter(UPDATE_TIME)) {
                this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("字段自动填充异常", e);
        }
    }

    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    private String getCurrentUsername() {
        // 默认实现，实际项目中需要根据具体的用户体系获取当前用户名
        return "admin";
    }
}