package com.chen.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ClientServerConfig {

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper(){
        return (authorities -> {
            Set<GrantedAuthority> mappedAuthorities=new HashSet<>();

            authorities.forEach(authority->{
                if(authority instanceof OAuth2UserAuthority oAuth2UserAuthority){
                    Object userAuthorities=oAuth2UserAuthority.getAttributes().get("authorities");
                    if(userAuthorities instanceof Collection<?> collection){
                        collection.stream().filter(a->a instanceof String)
                                .map(String::valueOf)
                                .map(SimpleGrantedAuthority::new)
                                .forEach(mappedAuthorities::add);
                    }
                }
            });
            return mappedAuthorities;
        });
    }
}
