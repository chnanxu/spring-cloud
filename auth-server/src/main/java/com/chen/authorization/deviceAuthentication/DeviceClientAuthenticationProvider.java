package com.chen.authorization.deviceAuthentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Slf4j
@RequiredArgsConstructor
public final class DeviceClientAuthenticationProvider implements AuthenticationProvider {

    private final RegisteredClientRepository registeredClientRepository;

    private static final String ERROR_URL="http://datatrancker.ietf.org/doc/html/rfc6749#section-3.2.1";


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DeviceClientAuthenticationToken deviceClientAuthentication=(DeviceClientAuthenticationToken) authentication;

        //只支持公共客户端
        if(!ClientAuthenticationMethod.NONE.equals(deviceClientAuthentication.getClientAuthenticationMethod())){
            return null;
        }


        //获取客户端id并查询
        String clientId=deviceClientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient=this.registeredClientRepository.findByClientId(clientId);
        if(registeredClient==null){
            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
        }

        if(log.isTraceEnabled()){
            log.trace("Retrieved registered client");
        }

        if(!registeredClient.getClientAuthenticationMethods().contains(
                deviceClientAuthentication.getClientAuthenticationMethod()
        )){
            throwInvalidClient("authentication_method");
        }

        if(log.isTraceEnabled()){
            log.trace("Validated device client authentication parameters");
        }

        if(log.isTraceEnabled()){
            log.trace("Authenticated device client");
        }

        return new DeviceClientAuthenticationToken(registeredClient,
                deviceClientAuthentication.getClientAuthenticationMethod(),null);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DeviceClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName){
        OAuth2Error error=new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Device client authentication failed:"+parameterName,
                ERROR_URL
        );
        throw new OAuth2AuthenticationException(error);
    }
}
