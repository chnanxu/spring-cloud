package com.chen.service;


import com.chen.mapper.PageMapper;
import com.chen.mapper.UserMapper;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.UserLikeComment;
import com.chen.utils.result.PageCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService{

    private final SimpleDateFormat systemTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final PageMapper pageMapper;

    private final RedisCache redisCache;

    private final UserMapper userMapper;

    private final UserDetailService userDetailService;

    @Override
    public String getReCommentUname(long to_commentID) {
        return pageMapper.getReCommentUname(to_commentID);
    }

    @Override
    public void deleteComment(long commentId) {
        pageMapper.deleteComment(commentId);
    }



    @Override
    public Item_Details getPageDetails(long pid) {

        pageMapper.addReadTimes(pid);

        return pageMapper.getPageDetails(pid);
    }

    @Override
    public List<Item_Details> getAuthorOther(String uid ,long pid) {



        return  pageMapper.getAuthorOtherByUid(uid,pid);
    }

    @Override
    public List<Item_Comments> getPageDetailsComments(long pid) {
        List<Item_Comments> rootComments;

        rootComments= pageMapper.getPageDetailsComments(pid);   //获取顶级评论列表


        return getItemComments(pid, rootComments);
    }

    @Override
    public List<Item_Comments> getAllSonComment(long pid,long comment_id){

        List<Item_Comments> rootComments= pageMapper.getAllSonComment(comment_id);

        return getItemComments(pid, rootComments);
    }
    private List<Item_Comments> getItemComments(long pid, List<Item_Comments> rootComments) {
        Oauth2UserinfoResult userInfo=userDetailService.getLoginUserInfo();
        if(userInfo==null){
            for (Item_Comments commentItem:rootComments   //获取顶级评论的前三条子评论列表
            ) {
                commentItem.setSonList(pageMapper.getSonComments(commentItem.getComment_id()));
            }
        }else{
            for (Item_Comments commentItem:rootComments   //获取顶级评论的前三条子评论列表
            ) {
                if(pageMapper.getUserLikeComments(userInfo.getUid(),pid,commentItem.getComment_id())!=null){    //用户是否点赞
                    commentItem.setUserLike(true);
                }
                commentItem.setSonList(pageMapper.getSonComments(commentItem.getComment_id()));
                for (Item_Comments sonCommentItem:commentItem.getSonList()
                ) {
                    if(pageMapper.getUserLikeComments(userInfo.getUid(),pid,sonCommentItem.getComment_id())!=null){  //用户是否点赞
                        sonCommentItem.setUserLike(true);
                    }
                }
            }
        }


        return rootComments;
    }

    @Override
    public ResponseResult<Item_Comments> submitComment(Item_Comments commentData) {


        if(SensitiveWordHelper.contains(commentData.getContent())){
            return new ResponseResult<>(PageCode.COMMENT_SENSITIVE_WORD, null);
        }else{
            pageMapper.submitComment(commentData);
            pageMapper.updateItemCommentSize(commentData.getPid());
            commentData.setSonList(new ArrayList<>());
            return new ResponseResult<>(PageCode.COMMENT_SUCCESS,commentData);
        }

    }

    @Override
    public ResponseResult<Item_Comments> submitReComment(Item_Comments commentData) {

        if(SensitiveWordHelper.contains(commentData.getContent())){
            return new ResponseResult<>(PageCode.COMMENT_SENSITIVE_WORD, null);
        }else{
            pageMapper.submitComment(commentData);
            pageMapper.updateItemCommentSize(commentData.getPid());
            return new ResponseResult<>(PageCode.COMMENT_SUCCESS,commentData);
        }

    }



    @Override
    public String onLikeComment(UserLikeComment userLikeComment) {

        if(pageMapper.getUserLikeComments(userLikeComment.getUid(),userLikeComment.getPid(),userLikeComment.getComment_id())!=null){
            userMapper.deleteUserLikeComment(userLikeComment.getUid(), userLikeComment.getPid(), userLikeComment.getComment_id());
            return "已取消点赞";
        }else{
            userMapper.addUserLikeComment(userLikeComment.getUid(), userLikeComment.getPid(), userLikeComment.getComment_id());
            return "已点赞";
        }

    }

    @Override
    public String onLikeDetails(String uid, long pid) {
        if(userMapper.getUserLikeDetails(uid,pid)!=null){
            userMapper.deleteUserLikeDetails(uid,pid);
            return "已取消喜欢";
        }else{
            userMapper.addUserLikeDetails(uid,pid,systemTime.format(new Date(System.currentTimeMillis())));
            return "已喜欢";
        }
    }

    @Override
    public ResponseResult<String> supportAuthor(My_Earnings item) {

        if(userMapper.reduceBalance(item.getAmount(),item.getUid())==1){

            pageMapper.supportAuthor(item);
            return new ResponseResult<>(PageCode.SUPPORT_SUCCESS);

        }else{
            return new ResponseResult<>(PageCode.SUPPORT_FAILURE);
        }


    }

    @Override
    public ResponseResult<String> reportProject(ReportItem reportItem) {

        if(userMapper.reportProject(reportItem)==1){

            return new ResponseResult<>(PageCode.REPORT_SUCCESS);
        }
        else{
            return new ResponseResult<>(PageCode.REPORT_FAILURE);
        }

    }
}
