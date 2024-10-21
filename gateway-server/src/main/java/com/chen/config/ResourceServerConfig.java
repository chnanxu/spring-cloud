package com.chen.config;

import com.chen.exception.AccessDeniedHandler;
import com.chen.exception.AuthenticationEntryPoint;
import com.chen.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootConfiguration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ResourceServerConfig{

    private final CustomSecurityProperties customSecurityProperties;

    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http){

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(ServerHttpSecurity.CorsSpec::disable);

        http.authorizeExchange((authorize)->authorize
                .pathMatchers(customSecurityProperties.getIgnoreUriList().toArray(new String[0])).permitAll()
                .anyExchange().authenticated()
        );

        //设置当前服务为资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer)->resourceServer
                .jwt(jwt->jwt
                        .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                )
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())

        );


        return http.build();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor preAuthorize(){
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize();
    }


    /**
     * 自定义jwt解析器，设置解析出来的权限信息的前缀于在jwt中的key
     * @return jwt解析器适配器 ReactiveJwtAuthenticationConverterAdapter
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置解析权限信息的前缀，设置为空是去掉前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        // 设置权限信息在jwt claims中的key
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(){
        NimbusReactiveJwtDecoder jwtDecoder =(NimbusReactiveJwtDecoder)
                ReactiveJwtDecoders.fromIssuerLocation(customSecurityProperties.getIssuerUrl());

        OAuth2TokenValidator<Jwt> withClockSkew=new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(60)),
                new JwtIssuerValidator(customSecurityProperties.getIssuerUrl())
        );

        jwtDecoder.setJwtValidator(withClockSkew);
        return jwtDecoder;
    }


    public ServerAuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPoint();
    }


    public ServerAccessDeniedHandler accessDeniedHandler(){

        return new AccessDeniedHandler();
    }



}
