package com.example.demo.configuration;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class RateLimitConfig {

    @Value("${rateLimit.qps}")
    private double qps;

    @Bean
    public RateLimiter getRateLimiter() {

        if(0 == this.qps){
            this.qps = 1.0D;
        }
        return RateLimiter.create(qps);
    }

  /*  @Bean
    public AtomicLong getAtomicLong(){
        return new AtomicLong();
    }*/

}
