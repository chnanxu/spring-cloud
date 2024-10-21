package com.chen.controller;


import com.chen.mapper.PageMapper;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.UserLikeComment;
import com.chen.service.PageService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import result.CommonCode;
import result.ResponseResult;

import java.util.List;

@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageMapper pageMapper;

    private final PageService pageService;

    //常规页面相关接口--------------------------------------------------------------------------------------------------------

    @GetMapping("/getPageDetails/{pid}")  //详细页面数据接口
    public ResponseResult<Item_Details> getPageDetails(@PathVariable long pid){
        
        return pageService.getPageDetails(pid);
    }

    @GetMapping("/getAuthorOther/{uid}/{pid}") //作者其他作品接口
    public ResponseResult<List<Item_Details>> getAuthorOther(@PathVariable String uid,@PathVariable long pid){


        return pageService.getAuthorOther(uid,pid);
    }

    @GetMapping("/getPageDetailsComments/{pid}")  //评论数据接口
    public ResponseResult<List<Item_Comments>> getPageDetailsComments(@PathVariable long pid){


        return pageService.getPageDetailsComments(pid);
    }


    @PostMapping("/getAllSonComment/{pid}")   //获取所有子评论
    public ResponseResult<List<Item_Comments>> getAllSonComment(@PathVariable long pid, @RequestBody long comment_id){


        List<Item_Comments> result=pageService.getAllSonComment(pid,comment_id);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }


    @GetMapping("/getReCommentUname/{to_commentID}")  //获取回复用户昵称
    public ResponseResult<String> getReCommentUname(@PathVariable long to_commentID){

        String result= pageService.getReCommentUname(to_commentID);
        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }






    //用户相关接口--------------------------------------------------------------------------------------------------------


    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/submitCommunityPost/{gid}")
    public ResponseResult<String> submitCommunityPost(@PathVariable String gid){

        return new ResponseResult<>(CommonCode.SUCCESS,"success");
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/submitComment")    //提交评论
    public ResponseResult<Item_Comments> submitComment(@RequestBody Item_Comments commentData){



        return pageService.submitComment(commentData);
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/submitReComment")  //回复评论
    public ResponseResult<Item_Comments> submitReComment(@RequestBody Item_Comments commentData){


        return pageService.submitReComment(commentData);
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/deleteComment")   //删除评论
    public ResponseResult<String> deleteComment(@RequestBody long comment_id){

        pageService.deleteComment(comment_id);

        return new ResponseResult<>(CommonCode.SUCCESS,"删除评论");
    }


    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/onLikeComment")   //点赞评论
    public ResponseResult<String> onLikeComment(@RequestBody UserLikeComment userLikeComment){

        String message= pageService.onLikeComment(userLikeComment);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/likeDetails/{uid}/{pid}")  //点赞作品
    public ResponseResult<String> likeDetails(@PathVariable String uid, @PathVariable long pid){

        String message= pageService.onLikeDetails(uid,pid);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/supportAuthor")
    public ResponseResult<String> supportAuthor(@RequestBody My_Earnings item){
        return pageService.supportAuthor(item);
    }

    @PreAuthorize("hasAuthority('system:write')")
    @PostMapping("/reportProject")
    public ResponseResult<String> reportProject(@RequestBody ReportItem reportItem){
        return pageService.reportProject(reportItem);
    }

}
