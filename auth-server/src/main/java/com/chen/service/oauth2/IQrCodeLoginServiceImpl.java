package com.chen.service.oauth2;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.chen.utils.qrCode.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import util.CustomSecurityProperties;
import util.RedisCache;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class IQrCodeLoginServiceImpl implements IQrCodeLoginService {

    private final RedisCache redisCache=new RedisCache();

    private final CustomSecurityProperties customSecurityProperties=new CustomSecurityProperties();

    private final RedisOAuth2AuthorizationService authorizationService;

    /**
     * 过期时间
     */
    private final long QR_CODE_INFO_TIMEOUT=60*10;

    /**
     * 二维码存储前缀
     */
    private final String QR_CODE_PREV="login:qrcode";

    private final RequestCache requestCache=new HttpSessionRequestCache();

    @Override
    public QrCodeGenerateResponse generateQrCode() {
        //生成二维码唯一id
        String qrCodeId= IdWorker.getIdStr();
        //生成二维码并转换为base64
        String pngQrCode= QrCodeUtil.generateAsBase64(qrCodeId,new QrConfig(),"png");
        QrCodeInfo qrCodeInfo= QrCodeInfo.builder()
                .qrCodeId(qrCodeId)
                .qrCodeStatus(0)
                .expiresTime(LocalDateTime.now().plusMinutes(2L))
                .build();

        //获取当前request
        RequestAttributes requestAttributes= RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            //获取当前Session
            HttpServletRequest request=((ServletRequestAttributes)requestAttributes).getRequest();
            HttpServletResponse response=((ServletRequestAttributes)requestAttributes).getResponse();
            DefaultSavedRequest savedRequest=(DefaultSavedRequest) this.requestCache.getRequest(request,response);
            if(savedRequest!=null){
                if(!UrlUtils.isAbsoluteUrl(customSecurityProperties.getLoginUrl())){
                    //获取查询参数与请求路径
                    String queryString=savedRequest.getQueryString();
                    String requestUri=savedRequest.getRequestURI();
                    //前后端不分离根据请求路径和请求参数跳转
                    qrCodeInfo.setBeforeLoginQueryString(queryString);
                    qrCodeInfo.setBeforeLoginRequestUri(requestUri);
                }

                //获取跳转登录之前访问url的 查询参数query parameters
                String[] scopes=savedRequest.getParameterValues("scope");
                if(!ObjectUtils.isEmpty(scopes)){
                    qrCodeInfo.setScopes(Set.of(scopes[0].split(" ")));
                }
                //前端可以根据scope 显示要获取的信息，或固定显示要获取的信息
            }
        }
        redisCache.setCacheObject(QR_CODE_PREV+qrCodeId,qrCodeInfo,QR_CODE_INFO_TIMEOUT);
        return new QrCodeGenerateResponse(qrCodeId,pngQrCode);
    }

    @Override
    public QrCodeLoginScanResponse scan(QrCodeLoginScanRequest loginScan) {

        Assert.hasLength(loginScan.getQrCodeId(),"二维码id不能位空");

        //校验二维码状态
        QrCodeInfo qrCodeInfo=redisCache.getCacheObject(QR_CODE_PREV+loginScan.getQrCodeId());
        if(qrCodeInfo==null){
            throw new RuntimeException("无效二维码");
        }

        //验证状态
        if(!Objects.equals(qrCodeInfo.getQrCodeStatus(),0)){
            throw new RuntimeException("二维码已经被扫描过了，无法重复扫描");
        }

        //二维码是否过期
        boolean qrCodeExpire=qrCodeInfo.getExpiresTime().isBefore(LocalDateTime.now());

        if(qrCodeExpire){
            throw new RuntimeException("二维码已过期");
        }

        QrCodeLoginScanResponse loginScanResponse=new QrCodeLoginScanResponse();

        //获取登录用户信息
        OAuth2Authorization oAuth2Authorization=this.getOAuth2Authorization();
        if(oAuth2Authorization==null){
            throw new OAuth2AuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN,"登录已过期",null)
            );
        }



        return loginScanResponse;
    }

    @Override
    public void consent(QrCodeLoginConsentRequest loginConsent) {

        Assert.hasLength(loginConsent.getQrCodeId(),"二维码ID不能为空");

        //校验二维码状态
        QrCodeInfo qrCodeInfo=redisCache.getCacheObject(QR_CODE_PREV+loginConsent.getQrCodeId());
        if(qrCodeInfo==null){
            throw new RuntimeException("无效二维码或二维码已过期");
        }

        //验证临时票据
        String qrCodeTicketKey=String.format("%s%s:%s",QR_CODE_PREV,loginConsent.getQrCodeId(),loginConsent.getQrCodeTicket());
        String redisQrCodeTicket=redisCache.getCacheObject(qrCodeTicketKey);
        if(!Objects.equals(redisQrCodeTicket,loginConsent.getQrCodeTicket())){
            if(log.isDebugEnabled()){
                log.debug("临时票据有误、临时票据失效(超过redis存活时间后确认)");
            }
            throw new RuntimeException("登录确认失败，请重新扫描");
        }
        //使用后删除
        redisCache.deleteObject(qrCodeTicketKey);

        //获取登录用户信息
        OAuth2Authorization authorization=this.getOAuth2Authorization();
        if(authorization==null){
            throw new OAuth2AuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN,"登录已过期",null)
            );
        }

        //app端使用密码模式、手机认证登录，不适用三方登录的情况
        UsernamePasswordAuthenticationToken authenticationToken=authorization.getAttribute(Principal.class.getName());

        //根据二维码id存储用户信息
        String redisUserinfoKey=String.format("%s%s:%s",QR_CODE_PREV,"userinfo",loginConsent.getQrCodeId());
        //存储用户信息
        redisCache.setCacheObject(redisUserinfoKey,authenticationToken,QR_CODE_INFO_TIMEOUT);

        //更新二维码信息的状态
        qrCodeInfo.setQrCodeStatus(2);
        redisCache.setCacheObject(QR_CODE_PREV+loginConsent.getQrCodeId(),qrCodeInfo,QR_CODE_INFO_TIMEOUT);

    }

    @Override
    public QrCodeLoginFetchResponse fetch(String qrCodeId) {
        //校验二维码状态
        QrCodeInfo qrCodeInfo=redisCache.getCacheObject(QR_CODE_PREV+qrCodeId);
        if(qrCodeInfo==null){
            throw new RuntimeException("无效二维码或二维码已过期");
        }

        QrCodeLoginFetchResponse loginFetchResponse=new QrCodeLoginFetchResponse();

        //设置二维码是否过期、状态
        loginFetchResponse.setQrCodeStatus(qrCodeInfo.getQrCodeStatus());
        loginFetchResponse.setExpired(qrCodeInfo.getExpiresTime().isBefore(LocalDateTime.now()));

        if(!Objects.equals(qrCodeInfo.getQrCodeStatus(),0)){
            //如果是已扫描/已确认
            loginFetchResponse.setUname(qrCodeInfo.getUname());
            loginFetchResponse.setAvatarUrl(qrCodeInfo.getAvatarUrl());
        }

        //如果是已确认，将之前扫描确认的用户信息放入当前session中
        if(Objects.equals(qrCodeInfo.getQrCodeStatus(),2)){

            //根据二维码id从redis中获取用户信息
            String redisUserInfoKey=String.format("%s%s:%s",QR_CODE_PREV,"userinfo",qrCodeId);
            UsernamePasswordAuthenticationToken authenticationToken=redisCache.getCacheObject(redisUserInfoKey);
            if(authenticationToken!=null){
                //获取当前request
                RequestAttributes requestAttributes=RequestContextHolder.getRequestAttributes();
                if(requestAttributes==null){
                    throw new RuntimeException("获取当前Request失败");
                }
                HttpServletRequest request=((ServletRequestAttributes)requestAttributes).getRequest();
                HttpSession session=request.getSession(Boolean.FALSE);
                if(session!=null){
                    //获取到认证信息后将之前扫码确认的用户信息放入当前session中
                    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY,new SecurityContextImpl(authenticationToken));

                    //操作成功后移除缓存
                    redisCache.deleteObject(QR_CODE_PREV+qrCodeId);

                    //删除用户信息，防止其他人重放请求
                    redisCache.deleteObject(redisUserInfoKey);

                    //填充二维码数据，设置跳转到登录之前的请求路径、查询参数和是否授权申请请求
                    loginFetchResponse.setBeforeLoginRequestUri(qrCodeInfo.getBeforeLoginRequestUri());
                    loginFetchResponse.setBeforeLoginQueryString(qrCodeInfo.getBeforeLoginQueryString());

                }
            }else{
                throw new RuntimeException("获取登录确认用户信息失败");
            }
        }

        return loginFetchResponse;
    }


    private OAuth2Authorization getOAuth2Authorization(){
        //校验登录状态
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            throw new InsufficientAuthenticationException("未登录");
        }
        if(authentication instanceof JwtAuthenticationToken jwtToken){
            //jwt处理
            String tokenValue=jwtToken.getToken().getTokenValue();
            return authorizationService.findByToken(tokenValue, OAuth2TokenType.ACCESS_TOKEN);
        }
        return null;
    }
}
