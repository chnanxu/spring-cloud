package com.chen.controller;


import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;

import com.chen.service.UserDetailService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.RedisCache;
import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.Objects;



@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserDetailService userDetailService;
    private final RedisCache redisCache;

    @PostMapping("/createUserIpLocation/{ip}")
    public ResponseResult createUserIpLocation(@PathVariable String ip, @RequestHeader HttpRequest headers){

        return userDetailService.createUserIpLocation(ip,headers);
    }

    @PostMapping("/reg")
    @ResponseBody
    public ResponseResult register(@RequestBody User user){
        String sysCheck=redisCache.getCacheObject("captcha");
        String regCheck=user.getCaptcha();

        if(!Objects.equals(sysCheck,regCheck)){
            return new ResponseResult(UserCode.REGCHECKFAILURE);  //验证码错误
        }


        if(userDetailService.findByName(user.getUsername())!=null){
            return new ResponseResult(UserCode.USEREXIST);   //用户已存在
        }else{

            userDetailService.register(user);

            return new ResponseResult(UserCode.REGISTSUCCESS);  //注册成功
        }
    }

    @RequestMapping("/syncUserLog")
    public ResponseResult<Oauth2UserinfoResult> syncUserLog(){

        return new ResponseResult<>(CommonCode.SUCCESS,userDetailService.syncUserLog());
    }


}

