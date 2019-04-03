local key = KEYS[1]
--限流大小
local limit = tonumber(ARGV[1])
local timeOut = ARGV[2]
local current = tonumber(redis.call("get", key) or "0")

if current >= limit then --如果超出限流大小
    return 0
else --请求数+1，并设置2秒过期
    redis.call("INCRBY", key,"1")
    redis.call("expire", key,timeOut)
    return 1
end