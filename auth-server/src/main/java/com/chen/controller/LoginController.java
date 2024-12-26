package com.chen.controller;


import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;

import com.chen.service.UserDetailService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.RedisCache;
import com.github.houbb.heaven.util.lang.StringUtil;
import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.Objects;

import static com.chen.utils.util.RedisConstants.EMAIL_CODE_KEY;


@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserDetailService userDetailService;


    @PostMapping("/createUserIpLocation/{ip}")
    public ResponseResult createUserIpLocation(@PathVariable String ip, @RequestHeader HttpRequest headers){

        return userDetailService.createUserIpLocation(ip,headers);
    }

    @PostMapping("/reg")
    @ResponseBody
    public ResponseResult register(@RequestBody User user){

       return userDetailService.register(user);

    }

    @RequestMapping("/syncUserLog")
    public ResponseResult<Oauth2UserinfoResult> syncUserLog(){

        return new ResponseResult<>(CommonCode.SUCCESS,userDetailService.syncUserLog());
    }


}

