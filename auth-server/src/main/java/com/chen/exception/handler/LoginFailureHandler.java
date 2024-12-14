package com.chen.exception.handler;

import cn.hutool.json.JSONUtil;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json,charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        ResponseResult result=new ResponseResult(UserCode.PASSWORDFAILURE,exception.getMessage());

        PrintWriter writer=response.getWriter();
        writer.write(JSONUtil.toJsonStr(result));
    }
}
