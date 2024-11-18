package com.chen.filter;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.chen.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonFilter implements GlobalFilter, Ordered {

    public static PathMatcher pathMatcher=new AntPathMatcher();

    private final CustomSecurityProperties properties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest httpRequest=exchange.getRequest().mutate()
                .header("domain-name",exchange.getRequest().getURI().toString())
                .build();

        if(properties.getIgnoreUriList()!=null && pathMatchUrl(properties.getIgnoreUriList(),httpRequest.getURI().getPath())){
            return chain.filter(exchange);
        }

        String authorization=httpRequest.getHeaders().getFirst("token");
        String token = StringUtils.substringAfter(authorization,"bearer ");


        if(authorization!=null){
            checkToken(token);
        }


        return chain.filter(exchange.mutate().request(httpRequest).build());
    }

    public boolean checkToken(String token){



        return false;
    }

    public boolean pathMatchUrl(List<String> ignoreUrlList, String path){

        for (String ignore:ignoreUrlList){
            if(match(ignore,path)){
                return true;
            }
        }
        return false;
    }

    public boolean match(String ignoreUrl,String path){
        return pathMatcher.match(ignoreUrl,path);
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
