package com.chen.controller.user;


import com.chen.pojo.record.Diary_Book;
import com.chen.service.user.RecordService;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    @GetMapping("/getDiaryBookList/{uid}")
    public ResponseResult<List<Diary_Book>> getDiaryBookList(@PathVariable String uid){
        return recordService.getDiaryBookList(uid);
    }

    @PostMapping("/newDiaryBook/{uid}")
    public ResponseResult<String> newDiaryBook(@PathVariable String uid){
        return recordService.newDiaryBookList(uid);
    }

}
