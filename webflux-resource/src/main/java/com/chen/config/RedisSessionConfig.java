package com.chen.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;


@EnableRedisWebSession(saveMode = SaveMode.ALWAYS)
@Configuration
@Slf4j
public class RedisSessionConfig {

//    @Bean
//    public WebSessionIdResolver webSessionIdResolver(){
//        return new CustomWebSessionIdResolver();
//    }

}
