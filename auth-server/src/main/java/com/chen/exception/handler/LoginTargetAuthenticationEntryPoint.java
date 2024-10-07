package com.chen.exception.handler;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final String deviceActivateUri;
    private final RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();

    public LoginTargetAuthenticationEntryPoint(String loginFormUrl,String deviceActivateUri){
        super(loginFormUrl);
        this.deviceActivateUri=deviceActivateUri;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String deviceVerificationUri = "/oauth2/device_verification";
        // 兼容设备码前后端分离
        if (request.getRequestURI().equals(deviceVerificationUri)
                && request.getMethod().equals(HttpMethod.POST.name())
                && UrlUtils.isAbsoluteUrl(deviceActivateUri)) {
            // 如果是请求验证设备激活码(user_code)时未登录并且设备码验证页面是前后端分离的那种则写回json
            ResponseResult success =new ResponseResult(CommonCode.FAIL, "登录已失效，请重新打开设备提供的验证地址");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtils.objectCovertToJson(success));
            response.getWriter().flush();
            return;
        }

        // 获取登录表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            // 不是绝对路径调用父类方法处理
            super.commence(request, response, authException);
            return;
        }

        String requestUrl = request.getHeader("domain-name");
        if (!ObjectUtils.isEmpty(request.getQueryString())) {
//            requestUrl.append("?").append(request.getQueryString());
//            requestUrl=requestUrl+request.getQueryString();
        }

        // 绝对路径在重定向前添加target参数
        String targetParameter = URLEncoder.encode(requestUrl, StandardCharsets.UTF_8);
        String str1=targetParameter.substring(0,targetParameter.indexOf("m"));
        String str2=targetParameter.substring(targetParameter.indexOf("m")+1);

        targetParameter=str1+"m%2Fapi%2Foauth"+str2;
        String targetUrl = loginForm + "?target=" + targetParameter;
        log.debug("重定向至前后端分离的登录页面：{}", targetUrl);

        ResponseResult result=new ResponseResult(UserCode.NOLOGIN,targetUrl);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtils.objectCovertToJson(result));
        response.getWriter().flush();

    }
}
