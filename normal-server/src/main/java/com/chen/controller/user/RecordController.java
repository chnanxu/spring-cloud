package com.chen.controller.user;


import com.chen.pojo.page.Posts;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.service.user.RecordService;

import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    @GetMapping(value={"/getUserProject/","/getUserProject/{keywords}/{pageNumber}/{pageSize}"})
    public ResponseResult<List<Posts>> getUserProject(@PathVariable(required = false) String keywords,@PathVariable Integer pageNumber,@PathVariable Integer pageSize){

        return recordService.getUserProject(keywords,pageNumber,pageSize);
    }

    @GetMapping("/getSubscribeCount/{uid}")
    public ResponseResult<Long> getSubscribeCount(@PathVariable String uid){
        return recordService.getSubscribeCount(uid);
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
