-- 获取参数
local key = KEYS[1]
local count = tonumber(ARGV[1])
local time = tonumber(ARGV[2])

-- 获取当前流量大小
local current = tonumber(redis.call('get', key) or "0")

-- 如果超出限流大小，返回0
if current >= count then
    return 0
end

-- 执行计数器自增
redis.call("INCRBY", key, 1)

-- 如果key不存在，则设置过期时间
if current == 0 then
    redis.call("EXPIRE", key, time)
end

-- 返回成功
return 1