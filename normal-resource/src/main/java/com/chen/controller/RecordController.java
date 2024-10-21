package com.chen.controller;


import com.chen.service.RecordService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import result.CommonCode;
import result.ResponseResult;

@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping(value = {"/getUserInfo/","/getUserInfo/{keywords}"})
    public ResponseResult getUserInfo(@PathVariable(required = false) String keywords){

        return new ResponseResult(CommonCode.SUCCESS,recordService.getUserInfo(keywords));
    }

    @GetMapping(value={"/getUserProject/","/getUserProject/{keywords}"})
    public ResponseResult getUserProject(@PathVariable(required = false) String keywords){

        return new ResponseResult(CommonCode.SUCCESS,recordService.getUserProject(keywords));
    }

}
