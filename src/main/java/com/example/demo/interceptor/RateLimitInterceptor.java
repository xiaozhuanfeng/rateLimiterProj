package com.example.demo.interceptor;

import com.example.demo.constant.ResponseEnum;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component("rateLimitInterceptor")
public class RateLimitInterceptor extends AbstractInterceptor{

    private static final String RATELIMIT_TYPE_ACQUIRE = "acquire";

    @Value("${rateLimit.type}")
    private String rateLimitType;

    @Value("${rateLimit.tryAcquire.permits}")
    private int permits;

    @Value("${rateLimit.tryAcquire.timeOut}")
    private long timeout;

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    protected ResponseEnum handleRequest(HttpServletRequest req) throws Exception {
        if(RATELIMIT_TYPE_ACQUIRE.equals(rateLimitType)){
            return passWaitReq(req);
        }else{
            return giveUpReqWhenExceedLimit(req);
        }
    }

    /**
     * 阻塞线程直到请求可以再授予许可
     * @return
     */
    private ResponseEnum passWaitReq(HttpServletRequest req){
        //permits,默认1，从RateLimiter获取指定许可数，该方法会被阻塞直到获取到请求数
        double waitTime = rateLimiter.acquire();
        log.info("请求成功......waitTime="+waitTime);
        return ResponseEnum.OK;
    }

    /**
     * 判断是否可以立即获取许可,否则放弃请求
     * @param req
     * @return
     */
    private ResponseEnum giveUpReqWhenExceedLimit(HttpServletRequest req){
        //timeout 时间内获取令牌,默认timeout=0L，如果可以则挂起等待相应时间并返回true，否则立即返回false
        if(0 == permits || 0L == timeout){
            if(!rateLimiter.tryAcquire()){
                log.warn("限流中......");
                return ResponseEnum.RATE_LIMIT;
            }
        }else{
            if(!rateLimiter.tryAcquire(permits,timeout, TimeUnit.MICROSECONDS)){
                log.warn("permits="+permits+",timeout="+timeout+",限流中......");
                return ResponseEnum.RATE_LIMIT;
            }
        }

        log.info("请求成功......");
        return ResponseEnum.OK;
    }
}
