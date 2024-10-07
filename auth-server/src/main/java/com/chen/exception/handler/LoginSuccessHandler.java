package com.chen.exception.handler;

import com.alibaba.fastjson.JSON;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.RedisCache;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RedisCache redisCache;

    @Autowired
    public LoginSuccessHandler(RedisCache redisCache){
        this.redisCache=redisCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        ResponseResult result=new ResponseResult(UserCode.LOGINSUCCESS,"");
        response.getWriter().write(JSON.toJSONString(result));

    }

}
