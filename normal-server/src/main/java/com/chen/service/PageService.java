package com.chen.service;

import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.UserLikeComment;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PageService {


    ResponseResult<Item_Details> getPageDetails(long pid);

    ResponseResult<List<Item_Details>> getAuthorOther(long pid);

    ResponseResult<List<Item_Comments>> getPageDetailsComments(long pid,int pageNumber);

    List<Item_Comments> getAllSonComment(long pid,long comment_id);

    ResponseResult<Item_Comments> submitComment(Item_Comments commentData);

    String onLikeComment(UserLikeComment userLikeComment);

    ResponseResult<Item_Comments> submitReComment(Item_Comments commentData);

    ResponseResult<Map> getReCommentUname(long to_commentID);

    void deleteComment(long commentId);


    String onLikeDetails(String uid, long pid);

    ResponseResult<String> supportAuthor(My_Earnings item);

    ResponseResult<String> reportProject(ReportItem reportItem);
}
