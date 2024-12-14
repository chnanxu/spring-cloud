package com.chen.config;

import com.chen.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Collections;

// 请求跨域
@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class CorsConfig {

    private final CustomSecurityProperties securityProperties;
    @Bean
    public CorsWebFilter corsWebFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1.允许来源
        corsConfiguration.setAllowedOriginPatterns(securityProperties.getAllowedOriginUrl());
        //2.允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        //3.允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        //4.允许凭证
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }

}