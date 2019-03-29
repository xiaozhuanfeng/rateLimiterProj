package com.example.demo.interceptor;

import com.example.demo.constant.ResponseEnum;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service("limitReqNumInterceptor")
public class LimitReqNumInterceptor extends AbstractInterceptor {

    //@Autowired
    //private AtomicLong atomic;

    private static final long LIMIT_NUM = 10L;

    /**
     * 过期时间
     */
    private static final long EXPIRE_SEC = 20L;

    /**
     * 定制  过期时间
     */
    private LoadingCache<String, AtomicLong> LoadingCache =
            CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_SEC, TimeUnit.SECONDS).build(new CacheLoader<String,
                    AtomicLong>() {
                //本地缓存没有命中时，调用load，并将结果缓存
                @Override
                public AtomicLong load(String aLong) throws Exception {
                    return new AtomicLong(0L);
                }
            });

    @Override
    protected ResponseEnum handleRequest(HttpServletRequest req) throws Exception {
        String uri = req.getRequestURI();
        AtomicLong atomic = LoadingCache.get(uri);
        System.out.println("uri="+uri+",当前请求数，number="+atomic.get());

        if(atomic.incrementAndGet() > LIMIT_NUM) {
            //拒绝请求
            log.warn("uri="+uri+"超出请求数>>>,LIMIT_NUM="+LIMIT_NUM);
            return ResponseEnum.RATE_LIMIT;
        }
        log.warn("请求通过....");
        return ResponseEnum.OK;
    }

   /* @Override
    protected ResponseEnum handleRequest(HttpServletRequest req) throws Exception {
        //AtomicLong 对请求数限流  注意：非线程安全，也无法对特定的请求定制
        if(atomic.incrementAndGet() > LIMIT_NUM) {
            //拒绝请求
            log.warn("超出请求数>>>,LIMIT_NUM="+LIMIT_NUM);
            return ResponseEnum.RATE_LIMIT;
        }
        log.warn("请求通过....");
        return ResponseEnum.OK;
    }*/

    //@Scheduled(cron="*/6 * * * * ?")
   /* private void process(){
        if(atomic.get() > LIMIT_NUM) {
            log.warn("请求达到上限，定时清理请求数>>>"+atomic.get());
            //启动定时任务，定时清空请求数
            atomic.set(0L);
            log.warn("定时清理后>>>"+atomic.get());
        }
    }*/

}
