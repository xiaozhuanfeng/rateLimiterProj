package com.example.demo.interceptor;

import com.example.demo.constant.ResponseEnum;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service("limitReqLuaInterceptor")
public class LimitReqLuaInterceptor extends AbstractInterceptor implements InitializingBean {

    private static final long LIMIT_NUM = 10L;

    /**
     * 过期时间
     */
    private static final long EXPIRE_SEC = 20L;

    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rateLimitReq.lua")));
    }

    @Override
    protected ResponseEnum handleRequest(HttpServletRequest req) throws Exception {
        String uri = req.getRequestURI();

        //KEY[1]
        List<String> keys = Lists.newArrayList(uri);

        //ARGV
        String[] argvs = new String[]{"10"};
        Long res = (Long) stringRedisTemplate.execute(redisScript, keys, argvs);

        if (0L == res) {
            //拒绝请求
            log.warn("uri="+uri+"超出请求数>>>,LIMIT_NUM="+LIMIT_NUM);
            return ResponseEnum.RATE_LIMIT;
        }
        return ResponseEnum.OK;
    }

}
