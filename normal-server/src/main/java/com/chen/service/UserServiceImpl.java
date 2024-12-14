package com.chen.service;


import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.chen.mapper.PageMapper;
import com.chen.mapper.UserMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Item_Details;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


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
    public ResponseResult<Map<String,List<Item_Details>>> getSubscribeProject(String queryDate,String uid,Integer pageNumber){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        Map<String,List<Item_Details>> result=new HashMap<>();

        //所有订阅用户
        if(uid.equals("all")){
            List<User_likeuser> likeUsers=userMapper.getUserSubscribe(user.getUid(),pageNumber*10-10);
            //订阅用户为空
            if(likeUsers==null){
                return new ResponseResult<>(CommonCode.SUCCESS);
            }else{
                //查询所有日期
                if(queryDate.equals("all")){
                    List<Item_Details> details=new ArrayList<>();
                    for (User_likeuser likeuser:likeUsers){
                        details.addAll(userMapper.getSubscribeProjectAll(likeuser.getLike_uid(),pageNumber*10-10));

                    }
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    List<Item_Details> dateGroup;
                    //循环比对，将同一天发布的内容存放到map对象中
                    try{
                        for(Item_Details detail:details){
                            date= dateFormat.parse(detail.getCreate_time());
                            dateGroup=new ArrayList<>();
                            for(Item_Details detail2:details){
                                if(date.equals(dateFormat.parse(detail2.getCreate_time()))){
                                    dateGroup.add(detail2);
                                }
                            }
                            result.put(dateFormat.format(date),dateGroup);
                        }
                        //将结果按日期降序排序 
                        result=sortByKey(result,true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    for (User_likeuser likeuser:likeUsers){
                        result.put(queryDate,userMapper.getSubscribeProject(queryDate,likeuser.getLike_uid(),pageNumber*10-10));
                    }
                }
                return new ResponseResult<>(CommonCode.SUCCESS,result);
            }

        }else{ //指定用户
            if(queryDate.equals("all")){
                result.put("all",userMapper.getSubscribeProjectAll(uid,pageNumber*10-10));
            }else{
                result.put(queryDate,userMapper.getSubscribeProject(queryDate,uid,pageNumber*10-10));
            }
            return new ResponseResult<>(CommonCode.SUCCESS,result);
        }


    }

    public static <K extends Comparable<? super K>,V> Map<K,V> sortByKey(Map<K,V> map,boolean isDesc){
        Map<K,V> result= Maps.newLinkedHashMap();
        if(isDesc){
            map.entrySet().stream().sorted(Map.Entry.<K,V>comparingByKey().reversed())
                    .forEachOrdered(e->result.put(e.getKey(),e.getValue()));
        }else{
            map.entrySet().stream().sorted(Map.Entry.<K,V>comparingByKey())
                    .forEachOrdered(e->result.put(e.getKey(),e.getValue()));
        }
        return result;
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
