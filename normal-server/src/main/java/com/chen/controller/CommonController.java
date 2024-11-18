package com.chen.controller;

import com.chen.service.CommonService;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/getUserBaseInfo/{uid}")
    public ResponseResult getUserBaseInfo(@PathVariable String uid){

        return commonService.getUserBaseInfo(uid);
    }

}
