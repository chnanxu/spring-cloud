package com.chen.controller;

import com.chen.mapper.UserMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;
import com.chen.pojo.user.User_likeuser;
import com.chen.service.UserDetailService;
import com.chen.service.UserService;


import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")   //用户接口
public class UserController {

    private final UserMapper userMapper;

    private final UserService userService;

    private final UserDetailService userDetailService;


    @PostMapping("/onClock")   //打卡
    public ResponseResult<Oauth2UserinfoResult> onClock(){
        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        return new ResponseResult<>(CommonCode.SUCCESS,userService.onClock(user));
    }

    @GetMapping("/syncUserLog")
    public ResponseResult<Oauth2UserinfoResult> syncUserLog(){

        return new ResponseResult<>(CommonCode.SUCCESS,userDetailService.syncUserLog());
    }

    @PostMapping("/home")   //个人信息接口
    public ResponseResult<Oauth2UserinfoResult> home(){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        return new ResponseResult<>(CommonCode.SUCCESS,user);
    }

    @GetMapping("/getUserLikeGroup")   //获取用户关注社区接口
    public ResponseResult<List<Community>> getUserLikeCommunityList(){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        List<Community> result=userMapper.getUserLikeCommunityList(user.getUid());

        return new ResponseResult<>(CommonCode.SUCCESS,result);

    }

    @GetMapping("/getUserSubscribe/{pageNumber}")
    public ResponseResult<List<User_likeuser>> getUserSubscribe(@PathVariable Integer pageNumber){
        return userService.getUserSubscribe(pageNumber);
    }

    @GetMapping("/getUserComments/{pageNum}")    //获取用户评论数据
    public ResponseResult<List<Item_Comments>> getUserComments(@PathVariable int pageNum){

        Oauth2UserinfoResult user = userDetailService.getLoginUserInfo();

        List<Item_Comments> result=userService.getUserComments(user.getUid(),pageNum*15-15);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping("/updateUserSignTime/{community_id}/{uid}/{sign_time}")    //社区访问日志
    public ResponseResult<String> updateUserSignTime(@PathVariable long community_id, @PathVariable String uid, @PathVariable String sign_time){


        return userService.updateUserSignTime(community_id,uid,sign_time);
    }

    @GetMapping("/getUserRecentLookCommunity/{uid}")  //获取用户最近访问社区接口
    public ResponseResult<List<Community>> getUserRecentLookCommunity(@PathVariable String uid){

        List<Community> result=userMapper.getUserRecentLookCommunity(uid);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @PostMapping("/upload/userImg")  //更新头像接口
    @ResponseBody
    public ResponseResult<String> upload(@RequestParam("file") MultipartFile file) throws Exception{

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String message= userService.updateHeadImg(file,user.getUid());

        return new ResponseResult<>(CommonCode.SUCCESS,message);

    }

    @PostMapping("/updateUserInfo")  //更新用户个人信息
    @ResponseBody
    public ResponseResult<User> updateUserInfo(@RequestBody User userInfo){

        userMapper.updateUserInfo(userInfo);

        return new ResponseResult<>(CommonCode.SUCCESS,userMapper.findByUid(userInfo.getUid()));
    }

    @PostMapping("/likeCommunity/{gid}/{uid}")  //用户关注社区
    public ResponseResult<String> likeCommunity(@PathVariable long gid, @PathVariable String uid){

        String message=userService.guanzhuCommunity(uid,gid);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/likeUser/{uid}")  //用户关注作者
    public ResponseResult<String> likeUser(@PathVariable String uid){

        Oauth2UserinfoResult loginUser=userDetailService.getLoginUserInfo();
        String message= userService.guanzhuUser(loginUser.getUid(),uid);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @GetMapping("/isLikeUser/{uid}")  //用户是否关注作者
    public ResponseResult<String> isLikeUser(@PathVariable String uid){

        Oauth2UserinfoResult loginUser=userDetailService.getLoginUserInfo();

        if(userMapper.getUserLikeUser(loginUser.getUid(),uid)!=null){
            return new ResponseResult<>(CommonCode.SUCCESS,"已关注");
        }else{
            return new ResponseResult<>(CommonCode.SUCCESS,"未关注");
        }
    }

    @GetMapping("/isLikeProject/{pid}")  //用户是否喜欢作品
    public ResponseResult<String> isLikeProject(@PathVariable long pid){

        Oauth2UserinfoResult loginUser=userDetailService.getLoginUserInfo();

        if(userMapper.getUserLikeProject(loginUser.getUid(),pid)!=null){
            return new ResponseResult<>(CommonCode.SUCCESS,"已点赞");
        }else{
            return new ResponseResult<>(CommonCode.SUCCESS,"未点赞");
        }
    }
}
