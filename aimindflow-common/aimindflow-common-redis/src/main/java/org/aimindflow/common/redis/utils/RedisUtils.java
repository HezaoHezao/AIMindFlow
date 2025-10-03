package org.aimindflow.common.redis.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.redis.config.RedisProperties;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    private final HashOperations<String, String, Object> hashOperations;
    private final ListOperations<String, Object> listOperations;
    private final SetOperations<String, Object> setOperations;
    private final ZSetOperations<String, Object> zSetOperations;
    private final RedisProperties redisProperties;

    /**
     * 获取完整的key
     *
     * @param key 原始key
     * @return 添加前缀后的key
     */
    private String getKey(String key) {
        return redisProperties.getCachePrefix() + key;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key     键
     * @param timeout 时间(秒)
     * @return 是否成功
     */
    public boolean expire(String key, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.expire(getKey(key), timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失效时间异常", e);
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return Optional.ofNullable(redisTemplate.getExpire(getKey(key), TimeUnit.SECONDS)).orElse(0L);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(key)));
        } catch (Exception e) {
            log.error("判断key是否存在异常", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个或多个值
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(getKey(key[0]));
            } else {
                List<String> keys = new ArrayList<>(key.length);
                for (String k : key) {
                    keys.add(getKey(k));
                }
                redisTemplate.delete(keys);
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return valueOperations.get(getKey(key));
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            valueOperations.set(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("缓存放入异常", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false失败
     */
    public boolean set(String key, Object value, long timeout) {
        try {
            if (timeout > 0) {
                valueOperations.set(getKey(key), value, timeout, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("缓存放入并设置时间异常", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 递增后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return valueOperations.increment(getKey(key), delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 递减后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return valueOperations.increment(getKey(key), -delta);
    }

    /**
     * HashGet
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    public Object hGet(String key, String item) {
        return hashOperations.get(getKey(key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, Object> hGetAll(String key) {
        return hashOperations.entries(getKey(key));
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true成功 false失败
     */
    public boolean hSetAll(String key, Map<String, Object> map) {
        try {
            hashOperations.putAll(getKey(key), map);
            return true;
        } catch (Exception e) {
            log.error("HashSet异常", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key     键
     * @param map     对应多个键值
     * @param timeout 时间(秒)
     * @return true成功 false失败
     */
    public boolean hSetAll(String key, Map<String, Object> map, long timeout) {
        try {
            hashOperations.putAll(getKey(key), map);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("HashSet并设置时间异常", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            hashOperations.put(getKey(key), item, value);
            return true;
        } catch (Exception e) {
            log.error("向hash表中放入数据异常", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建，并设置时间
     *
     * @param key     键
     * @param item    项
     * @param value   值
     * @param timeout 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true成功 false失败
     */
    public boolean hSet(String key, String item, Object value, long timeout) {
        try {
            hashOperations.put(getKey(key), item, value);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("向hash表中放入数据并设置时间异常", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键
     * @param item 项 可以是多个 不能为null
     */
    public void hDelete(String key, String... item) {
        hashOperations.delete(getKey(key), (Object[]) item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键
     * @param item 项
     * @return true存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return hashOperations.hasKey(getKey(key), item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 递增后的值
     */
    public double hIncr(String key, String item, double by) {
        return hashOperations.increment(getKey(key), item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少几(小于0)
     * @return 递减后的值
     */
    public double hDecr(String key, String item, double by) {
        return hashOperations.increment(getKey(key), item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set中的所有值
     */
    public Set<Object> sGet(String key) {
        try {
            return setOperations.members(getKey(key));
        } catch (Exception e) {
            log.error("根据key获取Set中的所有值异常", e);
            return Collections.emptySet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(setOperations.isMember(getKey(key), value));
        } catch (Exception e) {
            log.error("根据value从set中查询是否存在异常", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return setOperations.add(getKey(key), values);
        } catch (Exception e) {
            log.error("将数据放入set缓存异常", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存，并设置过期时间
     *
     * @param key     键
     * @param timeout 时间(秒)
     * @param values  值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, long timeout, Object... values) {
        try {
            Long count = setOperations.add(getKey(key), values);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return count;
        } catch (Exception e) {
            log.error("将set数据放入缓存并设置过期时间异常", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set缓存的长度
     */
    public long sSize(String key) {
        try {
            return setOperations.size(getKey(key));
        } catch (Exception e) {
            log.error("获取set缓存的长度异常", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
            return setOperations.remove(getKey(key), values);
        } catch (Exception e) {
            log.error("移除值为value的异常", e);
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0到-1代表所有值
     * @return list缓存的内容
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return listOperations.range(getKey(key), start, end);
        } catch (Exception e) {
            log.error("获取list缓存的内容异常", e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list缓存的长度
     */
    public long lSize(String key) {
        try {
            return listOperations.size(getKey(key));
        } catch (Exception e) {
            log.error("获取list缓存的长度异常", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return list中的值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return listOperations.index(getKey(key), index);
        } catch (Exception e) {
            log.error("通过索引获取list中的值异常", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, Object value) {
        try {
            listOperations.rightPush(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("将list放入缓存异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, Object value, long timeout) {
        try {
            listOperations.rightPush(getKey(key), value);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("将list放入缓存并设置过期时间异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSetAll(String key, List<Object> value) {
        try {
            listOperations.rightPushAll(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("将list放入缓存异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否成功
     */
    public boolean lSetAll(String key, List<Object> value, long timeout) {
        try {
            listOperations.rightPushAll(getKey(key), value);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("将list放入缓存并设置过期时间异常", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 是否成功
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            listOperations.set(getKey(key), index, value);
            return true;
        } catch (Exception e) {
            log.error("根据索引修改list中的某条数据异常", e);
            return false;
        }
    }

    /**
     * 移除N个值为value的项
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return listOperations.remove(getKey(key), count, value);
        } catch (Exception e) {
            log.error("移除N个值为value的项异常", e);
            return 0;
        }
    }

    /**
     * 获取指定前缀的所有key
     *
     * @param pattern 前缀
     * @return 匹配的key集合
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(getKey(pattern));
        } catch (Exception e) {
            log.error("获取指定前缀的所有key异常", e);
            return Collections.emptySet();
        }
    }
}