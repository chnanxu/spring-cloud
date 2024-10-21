package com.chen.authorization.SpringAuthorizationServer.Repository;

import com.chen.authorization.SpringAuthorizationServer.SupplierDeferredSecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import util.RedisCache;

import java.util.function.Supplier;


import static com.chen.utils.util.SecurityConstants.NONCE_HEADER_NAME;
import static util.RedisConstants.DEFAULT_TIMEOUT_SECONDS;
import static util.RedisConstants.SECURITY_CONTEXT_PREFIX_KEY;


@Component
@RequiredArgsConstructor
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private final RedisCache redisCache=new RedisCache();

    private final SecurityContextHolderStrategy securityContextHolderStrategy= SecurityContextHolder.getContextHolderStrategy();

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        throw new UnsupportedOperationException("Method deprecated:");
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String nonce=getNonce(request);
        if(ObjectUtils.isEmpty(nonce)){
            return;
        }

        SecurityContext emptyContext=this.securityContextHolderStrategy.createEmptyContext();
        if(emptyContext.equals(context)){
            redisCache.deleteObject(SECURITY_CONTEXT_PREFIX_KEY+nonce);
        }else{
            redisCache.setCacheObject(SECURITY_CONTEXT_PREFIX_KEY+nonce,context,DEFAULT_TIMEOUT_SECONDS);
        }

    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String nonce=getNonce(request);
        if(ObjectUtils.isEmpty(nonce)){
            return false;
        }

        return redisCache.getCacheObject(SECURITY_CONTEXT_PREFIX_KEY+nonce)!=null;
    }


    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier=()->readSecurityContextFromRedis(request);
        return new SupplierDeferredSecurityContext(supplier,this.securityContextHolderStrategy);
    }

    private SecurityContext readSecurityContextFromRedis(HttpServletRequest request) {
        if(request==null){
            return null;
        }

        String nonce=getNonce(request);
        if(ObjectUtils.isEmpty(nonce)){
            return null;
        }

        return redisCache.getCacheObject(SECURITY_CONTEXT_PREFIX_KEY+nonce);

    }


    private String getNonce(HttpServletRequest request){
        String nonce=request.getHeader(NONCE_HEADER_NAME);
        if(ObjectUtils.isEmpty(nonce)){
            nonce=request.getParameter(NONCE_HEADER_NAME);
            HttpSession session=request.getSession(Boolean.FALSE);
            if(ObjectUtils.isEmpty(nonce)&&session!=null){
                nonce=session.getId();
            }
        }
        return nonce;
    }

}
