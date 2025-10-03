package org.aimindflow.common.idempotent.constant;

/**
 * 幂等常量类
 *
 * @author HezaoHezao
 */
public class IdempotentConstants {

    /**
     * Redis Key前缀
     */
    public static final String REDIS_KEY_PREFIX = "idempotent:";

    /**
     * 默认过期时间，单位：秒（5分钟）
     */
    public static final int DEFAULT_EXPIRE_TIME = 300;

    /**
     * 默认提示消息
     */
    public static final String DEFAULT_MESSAGE = "请勿重复提交";

    /**
     * 幂等Key分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 错误码
     */
    public static final int ERROR_CODE = 4001;

    /**
     * 错误消息
     */
    public static final String ERROR_REPEAT_SUBMIT = "重复提交";
    public static final String ERROR_GENERATE_KEY = "生成幂等Key失败";
    public static final String ERROR_LOCK_ACQUIRE = "获取锁失败";
    public static final String ERROR_LOCK_RELEASE = "释放锁失败";

    /**
     * 私有构造函数，防止实例化
     */
    private IdempotentConstants() {
        throw new IllegalStateException("Constant class");
    }
}