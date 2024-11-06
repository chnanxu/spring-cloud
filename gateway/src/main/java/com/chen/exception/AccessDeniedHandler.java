package com.chen.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        Map<String,String> parameters=new LinkedHashMap<>();

        return exchange.getPrincipal()
                .filter(AbstractOAuth2TokenAuthenticationToken.class::isInstance)
                .map((token)->errorMessageParameters(parameters))
                .switchIfEmpty(Mono.just(parameters))
                .flatMap((params)->respond(exchange,params));
    }

    private static Map<String,String> errorMessageParameters(Map<String,String> parameters){
       parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
       parameters.put("error_description",
               "The request require higher privileges than provided by the access token.");
       return parameters;
    }

    private static Mono<Void> respond(ServerWebExchange exchange,Map<String,String> parameters){
        String wwwAuthenticate=computeWWWAuthenticateHeaderValue(parameters);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().set(HttpHeaders.WWW_AUTHENTICATE,wwwAuthenticate);
        return exchange.getResponse().setComplete();
    }

    private static String computeWWWAuthenticateHeaderValue(Map<String,String> parameters){
        StringBuilder wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");
        if(!parameters.isEmpty()){
            wwwAuthenticate.append(" ");
            int i=0;
            for(Map.Entry<String,String> entry : parameters.entrySet()){
                wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                if(i != parameters.size()-1){
                    wwwAuthenticate.append(", ");
                }
                i++;
            }
        }
        return wwwAuthenticate.toString();
    }

}
