package com.chen.controller;

import com.chen.pojo.StoreProject;
import com.chen.service.StoreService;
import com.chen.util.CommonCode;
import com.chen.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/getStoreIndex")
    public ResponseResult getStoreIndex(){

        List<StoreProject> result=storeService.getStoreIndex();

        return new ResponseResult(CommonCode.SUCCESS,result);
    }
}
