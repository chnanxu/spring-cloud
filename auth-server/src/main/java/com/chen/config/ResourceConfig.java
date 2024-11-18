package com.chen.config;

import com.chen.exception.handler.LoginFailureHandler;
import com.chen.exception.handler.LoginSuccessHandler;
import com.chen.utils.util.CustomSecurityProperties;
import com.chen.utils.util.RedisCache;
import com.chen.utils.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.CorsFilter;


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class ResourceConfig {

    private final CorsFilter corsFilter;

    private final CustomSecurityProperties customSecurityProperties;

    private final RedisCache redisCache;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)throws Exception{

        SecurityUtils.applyBasicSecurity(http,corsFilter,customSecurityProperties);

        http.authorizeHttpRequests((authorize)->authorize
                .anyRequest().permitAll()

        ).formLogin(formLogin->{
            if(UrlUtils.isAbsoluteUrl(customSecurityProperties.getLoginUrl())){
                formLogin.successHandler(new LoginSuccessHandler(redisCache));
                formLogin.failureHandler(new LoginFailureHandler());
            }
        }
        );


        return http.build();
    }



}
