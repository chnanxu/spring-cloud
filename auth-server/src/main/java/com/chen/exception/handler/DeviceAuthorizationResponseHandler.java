package com.chen.exception.handler;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 校验设备码成功响应类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class DeviceAuthorizationResponseHandler implements AuthenticationSuccessHandler {

    /**
     * 设备码验证成功后跳转地址
     */
    private final String deviceActivatedUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("设备码验证成功，响应JSON.");
        // 写回json数据
        ResponseResult result = new ResponseResult(CommonCode.SUCCESS,deviceActivatedUri);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtils.objectCovertToJson(result));
        response.getWriter().flush();
    }
}