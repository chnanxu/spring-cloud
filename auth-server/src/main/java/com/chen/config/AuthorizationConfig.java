package com.chen.config;

import com.chen.authorization.FederatedIdentityIdTokenCustomizer;
import com.chen.authorization.smsAuthentication.SmsCaptchaGrantAuthenticationConverter;
import com.chen.authorization.smsAuthentication.SmsCaptchaGrantAuthenticationProvider;

import com.chen.utils.util.*;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.CorsFilter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfig {

    private final CorsFilter corsFilter;
    private final RedisCache redisCache;
    private final CustomSecurityProperties customSecurityProperties;

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      RegisteredClientRepository registeredClientRepository,
                                                                      AuthorizationServerSettings authorizationServerSettings) throws Exception{

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        //基础认证配置
        SecurityUtils.applyBasicSecurity(http,corsFilter,customSecurityProperties);

        //设备码配置
        SecurityUtils.applyDeviceSecurity(http,customSecurityProperties,registeredClientRepository,authorizationServerSettings);

        //自定义短信认证登录转换器
        SmsCaptchaGrantAuthenticationConverter converter=new SmsCaptchaGrantAuthenticationConverter();

        //自定义短信认证登录认证提供
        SmsCaptchaGrantAuthenticationProvider provider=new SmsCaptchaGrantAuthenticationProvider();

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint->tokenEndpoint
                        .accessTokenRequestConverter(converter)
                        .authenticationProvider(provider));


        DefaultSecurityFilterChain build=http.build();

        OAuth2TokenGenerator<?> tokenGenerator=http.getSharedObject(OAuth2TokenGenerator.class);
        AuthenticationManager authenticationManager=http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService=http.getSharedObject(OAuth2AuthorizationService.class);

        provider.setTokenGenerator(tokenGenerator);
        provider.setAuthorizationService(authorizationService);
        provider.setAuthenticationManager(authenticationManager);

        return build;
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                .issuer(customSecurityProperties.getIssuerUrl())
                .build();
    }

    /**
     * 自定义Jwt ，将权限信息放入jwt中
     * @return
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(){
        return new FederatedIdentityIdTokenCustomizer();
    }

    /**
     * 自定义jwt解析器，设置解析出来的权限信息前缀与在jwt中的key
     * @return
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthorityPrefix("");

        grantedAuthoritiesConverter.setAuthoritiesClaimName(SecurityConstants.AUTHORITIES_KEY);

        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;

    }

    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource(){
        String jwkSetCache=redisCache.getCacheObject(RedisConstants.AUTHORIZATION_JWS_PREFIX_KEY);
        if(ObjectUtils.isEmpty(jwkSetCache)){
            KeyPair keyPair=generateRsaKey();
            RSAPublicKey publicKey=(RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey=(RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey=new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            //生成jws
            JWKSet jwkSet=new JWKSet(rsaKey);
            //转为json字符串
            String jwkSetString= jwkSet.toString(Boolean.FALSE);
            //存入redis
            redisCache.setCacheObject(RedisConstants.AUTHORIZATION_JWS_PREFIX_KEY,jwkSetString);

            return new ImmutableJWKSet<>(jwkSet);
        }
        JWKSet jwkSet=JWKSet.parse(jwkSetCache);
        return new ImmutableJWKSet<>(jwkSet);
    }


    /**
     * 生成rsa密钥对，提供给jwk
     * @return
     */
    private static KeyPair generateRsaKey(){
        KeyPair keyPair;
        try{
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair=keyPairGenerator.generateKeyPair();
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    /**
     * jwt解析器
     * @param jwkSource
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();
    }

}
