package com.chen.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;


@Component
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        return Mono.defer(()->{
            String message="";
            if(ex.getMessage().startsWith("Jwt expired")){
                message="token expired";
            }
            System.out.println(ex.getMessage());
            HttpStatus status=getStatus(ex);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(status);
            response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE,message);
            return response.setComplete();
        });

    }

    public HttpStatus getStatus(AuthenticationException authEx){
        if(authEx instanceof OAuth2AuthenticationException ){
            OAuth2Error error=((OAuth2AuthenticationException) authEx).getError();
            if(error instanceof BearerTokenError){
                return ((BearerTokenError) error).getHttpStatus();
            }
        }
        return HttpStatus.UNAUTHORIZED;
    }
}
