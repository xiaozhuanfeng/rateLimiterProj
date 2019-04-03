package com.example.demo.interceptor;

import com.example.demo.constant.ResponseEnum;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String LIMIT_NUM = "10";

    /**
     * 过期时间
     */
    private static final String EXPIRE_SEC = "20";

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

        //KEY  这里可以控制细粒度，如：ip+uri+参数
        List<String> keys = Lists.newArrayList(uri);
        //ARGV
        String[] argvs = new String[]{LIMIT_NUM, EXPIRE_SEC};

        try {
            Long res = (Long) stringRedisTemplate.execute(redisScript, keys, argvs);

            if (0L == res) {
                //拒绝请求
                log.warn("uri=" + uri + "超出请求数>>>,LIMIT_NUM=" + LIMIT_NUM);
                return ResponseEnum.RATE_LIMIT;
            }
        } catch (Exception e) {
            log.error("执行lua异常,受限请求皆通过>>>>", e);
        }

        return ResponseEnum.OK;
    }

}
