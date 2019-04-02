package com.example.demo.configuration;

import com.example.demo.interceptor.CalculateReqInterceptor;
import com.example.demo.interceptor.LimitReqLuaInterceptor;
import com.example.demo.interceptor.LimitReqNumInterceptor;
import com.example.demo.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebInterceptorConfig extends WebMvcConfigurationSupport {

    @Bean
    public RateLimitInterceptor getRateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Bean
    public CalculateReqInterceptor getCalculateReqInterceptor() {
        return new CalculateReqInterceptor();
    }

    @Bean
    public LimitReqNumInterceptor getLimitReqNumInterceptor() {
        return new LimitReqNumInterceptor();
    }

    @Bean
    public LimitReqLuaInterceptor getLimitReqLuaInterceptor() {
        return new LimitReqLuaInterceptor();
    }


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration rateLimitIcpt = registry.addInterceptor(getRateLimitInterceptor());
        // 拦截配置
        rateLimitIcpt.addPathPatterns("/rateLimit/**");

        //请求数量拦截
        InterceptorRegistration limitReqNumIcpt = registry.addInterceptor(getLimitReqNumInterceptor());
        limitReqNumIcpt.addPathPatterns("/numLimit/**");

        //需要计算请求时间的请求
        InterceptorRegistration calculateIcpt = registry.addInterceptor(getCalculateReqInterceptor());
        calculateIcpt.addPathPatterns("/calculate/**");

        //lua + redis 拦截
        InterceptorRegistration luaIcpt = registry.addInterceptor(getLimitReqLuaInterceptor());
        luaIcpt.addPathPatterns("/luaLimit/**");
    }
}
