package com.example.demo.interceptor;

import com.example.demo.constant.ResponseEnum;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component("calculateReqInterceptor")
public class CalculateReqInterceptor extends HandlerInterceptorAdapter {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private LocalDateTime startTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        this.startTime = LocalDateTime.now();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        Long timeConsuming = Duration.between(this.startTime,LocalDateTime.now()).toMillis();
        //StringBuffer urlBuffer = request.getRequestURL();
        String uri = request.getRequestURI();
        log.info("url="+uri+",耗时="+timeConsuming / 1000 +"s");
    }
}
