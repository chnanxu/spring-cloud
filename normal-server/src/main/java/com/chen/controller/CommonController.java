package com.chen.controller;

import com.chen.service.CommonService;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/getUserBaseInfo/{uid}")
    public ResponseResult<Map<String,Object>> getUserBaseInfo(@PathVariable String uid){

        return commonService.getUserBaseInfo(uid);
    }

    @PreAuthorize("hasAuthority('system:user')")
    @GetMapping("/preauth/test")
    public String preauthTest(){
        return "test";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

//    @RequestMapping(value = {"/ws/plugins/get","/ws/plugins/post"})
//    public ResponseResult getPlugins(@RequestParam("token") String token){
//
//        System.out.println(token);
//
//        return ResponseResult.success();
//    }
}
