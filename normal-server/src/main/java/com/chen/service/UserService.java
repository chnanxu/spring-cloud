package com.chen.service;


import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;
import com.chen.pojo.user.UserPersonalize;
import com.chen.pojo.user.User_likeuser;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {

    Oauth2UserinfoResult onClock(Oauth2UserinfoResult user);

    String guanzhuUser(String uid,String like_uid);

    String guanzhuCommunity(String uid,long gid);

    String updateHeadImg(MultipartFile file,String uid);

    List<Community> getUserLikeCommunity(String uid);

    ResponseResult<List<User_likeuser>> getUserSubscribe(Integer pageNumber);

    List<Item_Comments> getUserComments(String uid,int pageNum);

    ResponseResult<String> updateUserSignTime(long community_id, String uid, String last_sign_time);

}
