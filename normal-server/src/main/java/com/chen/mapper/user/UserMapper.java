package com.chen.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    User findByUid(String uid);  //根据uid查找

    @Select("select * from user_privacy where uid=#{uid}")
    UserPrivacy getUserPrivacy(String uid);

    List<UserPersonalize> getUserPersonalize(String uid); //用户个性化内容

    UserPersonalize getUserPersonalizeByType_id(String uid,Integer type_id);  //用户指定类型个性化内容

    void addUserPersonalizeTimes(String uid,Integer type_id); //更新用户访问个性化内容时间

    void createUserPersonalize(String uid,Integer type_id,String type_name);  //更新用户个性化内容

    void onClock(String uid);  //打卡

    void syncUserInfo(Oauth2UserinfoResult user);  //同步用户信息

    void updateUserImg(String uid, String user_img);  //更新头像

    void updateUserInfo(User userInfo);   //更新用户信息

    void addUserLikeComment(String uid, String pid, long comment_id);  //点赞

    void deleteUserLikeComment(String uid, String pid, long comment_id);  //取消点赞

    void addLikeCommunity(String uid, long community_id);   //关注社区

    Community getUserLikeCommunity(String uid, long community_id);  //单个查询社区是否关注

    void removeCommunity(String uid, long community_id);    //取消关注社区

    UserLikeDetails getUserLikeDetails(String uid, String pid);   //获取用户是否推荐该作品

    void deleteUserLikeDetails(String uid, String pid);   //取消推荐作品

    void addUserLikeDetails(String uid, String pid,String like_time); //推荐作品

    List<Community> getUserLikeCommunityList(String uid);   //获取用户关注社区列表

    List<User_likeuser> getUserSubscribe(String uid,Integer pageNumber); //获取用户订阅列表

    List<Posts> getSubscribeProject(String queryDate, String uid, Integer pageNumber);//根据日期，获取订阅用户作品列表

    List<Posts> getSubscribeProjectAll(String uid, Integer pageNumber);  //获取订阅用户全部作品

    List<Item_Comments> getUserComments(String uid,int pageNum);  //获取用户评论列表

    List<Community> getUserRecentLookCommunity(String uid);  //获取用户最近访问社区列表

    Community getUserLookCommunity(String uid, long community_id);  //获取用户最近访问社区

    void insertUserLookCommunity(String uid,long community_id,String last_sign_time); //若之前没访问过该社区则插入表中

    void updateUserSignTime(String uid, long community_id, String last_sign_time);    //更新用户访问社区时间

    void updateCommunityHotTimes(long community_id);   //更新社区热度

    User_likeuser getUserLikeUser(String uid,String like_uid);   //获取用户订阅情况

    void likeUser(String uid,String like_uid);  //订阅用户

    void cancelLikeUser(String uid,String like_uid);    //取消订阅用户

    UserLikeDetails getUserLikeProject(String uid,String pid);    //获取用户点赞作品

    int reduceBalance(float amount,String uid); //扣款

    int reportProject(ReportItem reportItem); //举报

}
