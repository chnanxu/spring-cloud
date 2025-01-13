package com.chen.controller;


import com.chen.mapper.PageMapper;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.UserLikeComment;
import com.chen.service.PageService;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageMapper pageMapper;

    private final PageService pageService;

    //常规页面相关接口--------------------------------------------------------------------------------------------------------

    @GetMapping("/getPageDetails/{pid}")  //详细页面数据接口
    public ResponseResult<Posts> getPageDetails(@PathVariable String pid){
        
        return pageService.getPageDetails(pid);
    }

    @GetMapping("/getAuthorOther/{authorUid}/{pid}") //作者其他作品接口
    public ResponseResult<List<Posts>> getAuthorOther(@PathVariable String authorUid,@PathVariable String pid){

        return pageService.getAuthorOther(authorUid,pid);
    }

    @GetMapping("/getPageDetailsComments/{pid}/{pageNumber}")  //评论数据接口
    public ResponseResult<List<Item_Comments>> getPageDetailsComments(@PathVariable String pid,@PathVariable int pageNumber){

        return pageService.getPageDetailsComments(pid,pageNumber);
    }


    @PostMapping("/getAllSonComment/{pid}")   //获取所有子评论
    public ResponseResult<List<Item_Comments>> getAllSonComment(@PathVariable String pid, @RequestBody long comment_id){


        List<Item_Comments> result=pageService.getAllSonComment(pid,comment_id);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }


    @GetMapping("/getReCommentUname/{to_commentID}")  //获取回复用户昵称
    public ResponseResult<Map> getReCommentUname(@PathVariable long to_commentID){


        return pageService.getReCommentUname(to_commentID);
    }






    //用户相关接口--------------------------------------------------------------------------------------------------------


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
    public ResponseResult<String> likeDetails(@PathVariable String uid, @PathVariable String pid){

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
