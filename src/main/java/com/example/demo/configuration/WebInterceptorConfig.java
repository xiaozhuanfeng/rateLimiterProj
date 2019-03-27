package com.example.demo.configuration;

import com.example.demo.interceptor.CalculateReqInterceptor;
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

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration rateLimitIcpt = registry.addInterceptor(getRateLimitInterceptor());
        // 拦截配置
        rateLimitIcpt.addPathPatterns("/rateLimit/**");

        //需要计算请求时间的请求
        InterceptorRegistration calculateIcpt = registry.addInterceptor(getCalculateReqInterceptor());
        calculateIcpt.addPathPatterns("/calculate/**");
    }
}
