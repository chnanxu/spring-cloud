package com.chen.exception.handler;

import cn.hutool.json.JSONUtil;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        System.out.println(accessDeniedException);
        response.setContentType("application/json,charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        ResponseResult result=new ResponseResult(UserCode.NOPERMISSION,accessDeniedException);
        PrintWriter writer=response.getWriter();
        writer.write(JSONUtil.toJsonStr(result));
    }
}
