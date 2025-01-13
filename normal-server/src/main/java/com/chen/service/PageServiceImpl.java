package com.chen.service;


import com.chen.mapper.PageMapper;
import com.chen.mapper.user.UserMapper;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.UserLikeComment;

import com.chen.repository.MongoDataPageAble;
import com.chen.repository.create.PostRepository;
import com.chen.service.user.UserDetailService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.PageCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService{

    private final SimpleDateFormat systemTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final PageMapper pageMapper;

    private final RedisCache redisCache;

    private final UserMapper userMapper;

    private final UserDetailService userDetailService;
    private final PostRepository postRepository;

    @Override  //获取回复用户名称
    public ResponseResult<Map> getReCommentUname(long to_commentID) {
        
        return new ResponseResult<>(CommonCode.SUCCESS,pageMapper.getReCommentUname(to_commentID));
    }

    @Override  //删除评论
    public void deleteComment(long commentId) {
        pageMapper.deleteComment(commentId);
    }



    @Override  //获取页面详情
    public ResponseResult<Posts> getPageDetails(String pid) {


        Posts result= postRepository.findById(pid).orElse(null);
        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();


        if(result==null){
            return new ResponseResult<>(PageCode.DETAIL_TAKEOFF,null);
        }else{

            if(user.getUid()!=null){
                if(userMapper.getUserPersonalizeByType_id(user.getUid(),result.getTypeId())!=null){
                    userMapper.addUserPersonalizeTimes(user.getUid(),result.getTypeId());
                }else{
                    userMapper.createUserPersonalize(user.getUid(),result.getTypeId(),result.getTypeName());
                }
            }
            return new ResponseResult<>(PageCode.GET_SUCCESS,result);

        }

    }

    @Override   //获取作者其他作品
    public ResponseResult<List<Posts>> getAuthorOther(String authorUid,String pid) {
        MongoDataPageAble pageAble=new MongoDataPageAble(1,5, Sort.by(Sort.Direction.DESC,"createTime"));
        List<Posts> result=postRepository.findByUidAndPidNotOrderByCreateTimeDesc(authorUid,pid,pageAble).stream().toList();
        
        return new ResponseResult<>(PageCode.GET_SUCCESS,result);
    }

    @Override    //获取页面评论
    public ResponseResult<List<Item_Comments>> getPageDetailsComments(String pid,int pageNumber) {
        List<Item_Comments> rootComments;

        rootComments= pageMapper.getPageDetailsComments(pid,pageNumber*8-8);   //获取顶级评论列表

        return new ResponseResult<>(PageCode.GET_SUCCESS,getItemComments(pid, rootComments));
    }

    @Override   //获取全部子评论
    public List<Item_Comments> getAllSonComment(String pid,long comment_id){

        List<Item_Comments> rootComments= pageMapper.getAllSonComment(comment_id);

        return getItemComments(pid, rootComments);
    }
    //获取子评论逻辑实现
    private List<Item_Comments> getItemComments(String pid, List<Item_Comments> rootComments) {
        Oauth2UserinfoResult userInfo=userDetailService.getLoginUserInfo();
        if(userInfo==null){
            for (Item_Comments commentItem:rootComments   //获取顶级评论的前三条子评论列表
            ) {
                commentItem.setSonList(pageMapper.getSonComments(commentItem.getCommentId()));
            }
        }else{
            for (Item_Comments commentItem:rootComments   //获取顶级评论的前三条子评论列表
            ) {
                if(pageMapper.getUserLikeComments(userInfo.getUid(),pid,commentItem.getCommentId())!=null){    //用户是否点赞
                    commentItem.setUserLike(true);
                }
                commentItem.setSonList(pageMapper.getSonComments(commentItem.getCommentId()));
                for (Item_Comments sonCommentItem:commentItem.getSonList()
                ) {
                    if(pageMapper.getUserLikeComments(userInfo.getUid(),pid,sonCommentItem.getCommentId())!=null){  //用户是否点赞
                        sonCommentItem.setUserLike(true);
                    }
                }
            }
        }


        return rootComments;
    }

    @Override   //提交评论
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

    @Override   //提交回复评论
    public ResponseResult<Item_Comments> submitReComment(Item_Comments commentData) {

        if(SensitiveWordHelper.contains(commentData.getContent())){
            return new ResponseResult<>(PageCode.COMMENT_SENSITIVE_WORD, null);
        }else{
            pageMapper.submitComment(commentData);
            pageMapper.updateItemCommentSize(commentData.getPid());
            return new ResponseResult<>(PageCode.COMMENT_SUCCESS,commentData);
        }

    }



    @Override   //点赞评论
    public String onLikeComment(UserLikeComment userLikeComment) {

        if(pageMapper.getUserLikeComments(userLikeComment.getUid(),userLikeComment.getPid(),userLikeComment.getCommentId())!=null){
            userMapper.deleteUserLikeComment(userLikeComment.getUid(), userLikeComment.getPid(), userLikeComment.getCommentId());
            pageMapper.subtractCommentLikeTimes(userLikeComment.getCommentId());
            return "已取消点赞";
        }else{
            userMapper.addUserLikeComment(userLikeComment.getUid(), userLikeComment.getPid(), userLikeComment.getCommentId());
            pageMapper.addCommentLikeTimes(userLikeComment.getCommentId());
            return "已点赞";
        }

    }

    @Override //点赞作品
    public String onLikeDetails(String uid, String pid) {
        if(userMapper.getUserLikeDetails(uid,pid)!=null){
            userMapper.deleteUserLikeDetails(uid,pid);
            pageMapper.subtractDetailLikeTimes(pid);
            return "已取消喜欢";
        }else{
            userMapper.addUserLikeDetails(uid,pid,systemTime.format(new Date(System.currentTimeMillis())));
            pageMapper.addDetailLikeTimes(pid);
            return "已喜欢";
        }
    }

    @Override  //支持作者
    public ResponseResult<String> supportAuthor(My_Earnings item) {

        if(userMapper.reduceBalance(item.getAmount(),item.getUid())==1){

            pageMapper.supportAuthor(item);
            return new ResponseResult<>(PageCode.SUPPORT_SUCCESS);

        }else{
            return new ResponseResult<>(PageCode.SUPPORT_FAILURE);
        }


    }

    @Override   //举报作品
    public ResponseResult<String> reportProject(ReportItem reportItem) {

        if(userMapper.reportProject(reportItem)==1){

            return new ResponseResult<>(PageCode.REPORT_SUCCESS);
        }
        else{
            return new ResponseResult<>(PageCode.REPORT_FAILURE);
        }

    }
}
