package com.chen.service;


import com.chen.mapper.PageMapper;
import com.chen.mapper.UserMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;
import com.chen.pojo.user.UserPersonalize;
import com.chen.pojo.user.User_likeuser;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final PageMapper pageMapper;

    private final UserDetailService userDetailService;

    private final CustomSecurityProperties customSecurityProperties;



    @Override
    public Oauth2UserinfoResult onClock(Oauth2UserinfoResult user) {
        userMapper.onClock(user.getUid());
        user=userDetailService.syncUserLog();
        return user;

    }

    @Override   //关注作者实现
    public String guanzhuUser(String uid,String like_uid){
        String message="";
        if(userMapper.getUserLikeUser(uid,like_uid)!=null){
            userMapper.cancelLikeUser(uid,like_uid);
            message="已取消关注";
        }else{
            userMapper.likeUser(uid,like_uid);
            message="已关注";
        }
        return message;
    }

    @Override   //关注社区实现
    public String guanzhuCommunity(String uid,long gid){
        String message="";
        if(userMapper.getUserLikeCommunity(uid,gid)!=null){
            userMapper.removeCommunity(uid,gid);
            message="已退出该社区";
        }else{
            userMapper.addLikeCommunity(uid,gid);
            message="已加入该社区";
        }
        return message;
    }

    @Override    //更新头像实现
    public String updateHeadImg(MultipartFile file,String uid){

        String fileName=file.getOriginalFilename();

        String path=customSecurityProperties.getStaticPath()+"\\images\\user_data\\"+uid;

        String newFileName="user_headImg"+fileName.substring(fileName.lastIndexOf("."));

        File f=new File(path);

        if(!f.exists()){
            f.mkdirs();
        }

        String url="images/user_data/"+uid+"/"+newFileName;

        userMapper.updateUserImg(uid,url);

        try {
            file.transferTo(new File(path+"\\"+newFileName));
        } catch (IOException e) {
            e.printStackTrace();

            return "异常情况";
        }

        return url;
    }

    @Override
    public List<Community> getUserLikeCommunity(String uid) {
        return userMapper.getUserLikeCommunityList(uid);
    }

    @Override
    public ResponseResult<List<User_likeuser>> getUserSubscribe(Integer pageNumber) {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        if(user.getUid()!=null){
            List<User_likeuser> result=userMapper.getUserSubscribe(user.getUid(),pageNumber*10-10);
            return new ResponseResult<>(CommonCode.SUCCESS,result);
        }else{
            return new ResponseResult<>(UserCode.NOLOGIN);
        }

    }

    @Override
    public List<Item_Comments> getUserComments(String uid,int pageNum) {

        List<Item_Comments> result=userMapper.getUserComments(uid,pageNum);

        for (Item_Comments item:result) {
            item.setItem_detail(pageMapper.getPageDetails(item.getPid()));
        }

        return result;
    }

    @Override
    public ResponseResult<String> updateUserSignTime(long community_id, String uid, String last_sign_time) {

        try{

            if(userMapper.getUserLookCommunity(uid,community_id)!=null){
                userMapper.updateUserSignTime(uid,community_id,last_sign_time);
                userMapper.updateCommunityHotTimes(community_id);
            }else{
                userMapper.insertUserLookCommunity(uid,community_id,last_sign_time);
                userMapper.updateCommunityHotTimes(community_id);
            }

            return new ResponseResult<>(CommonCode.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseResult<>(CommonCode.FAIL);
    }

}
