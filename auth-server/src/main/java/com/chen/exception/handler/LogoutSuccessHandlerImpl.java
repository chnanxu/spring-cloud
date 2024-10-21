package com.chen.exception.handler;

import com.alibaba.fastjson.JSON;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import util.RedisCache;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


    private final RedisCache redisCache=new RedisCache();
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        System.out.println(request);
        System.out.println(response);
        System.out.println(authentication);

        ResponseResult result=new ResponseResult(CommonCode.SUCCESS,"退出登录");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
