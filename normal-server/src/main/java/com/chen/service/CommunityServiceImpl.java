package com.chen.service;


import com.chen.mapper.CommunityMapper;
import com.chen.mapper.user.UserMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Posts;
import com.chen.pojo.user.Oauth2UserinfoResult;

import com.chen.service.user.UserDetailService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityMapper communityMapper;
    private final UserDetailService userDetailService;
    private final UserMapper userMapper;


    @Override
    public ResponseResult<List<Map>> getCommunityListByQueryType(Integer queryType,int pageNum) {

        List<Map> result;
        if(queryType==0){
            result=communityMapper.getCommunityList(pageNum);
        }else{
            result=communityMapper.getCommunityListByType(queryType,pageNum);
        }

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }
    @Override
    public List<Community> getCommunity(){

        List<Community> result=communityMapper.getCommunity();
        Oauth2UserinfoResult userInfo=userDetailService.getLoginUserInfo();

        //用户是否登录
        if(userInfo!=null){

            for (Community item:result
            ) {
                if(userMapper.getUserLikeCommunity(userInfo.getUid(),item.getCommunity_id())!=null){
                    item.setUserLike(true);
                }
            }
        }

        return result;
    }

    @Override
    public List<Map> getTotalHotCommunity(){

        return communityMapper.getTotalHotCommunity();
    }

    @Override
    public List<CommunityLeftNav> getCommunityLeftNav(long community_id) {
        return communityMapper.getCommunityLeftNav(community_id);
    }

    @Override
    public ResponseResult<List<CommunityModule>> getCommunityModule(long community_id) {
        return new ResponseResult<>(CommonCode.SUCCESS,communityMapper.getCommunityModule(community_id));
    }


    @Override
    public List<Posts> getCommunityDetailsBySortType(long community_id, int pageNum, String sortType) {
        List<Posts> result;

        LocalDate queryTimeParameters=LocalDate.now();

        switch (sortType){

            case "week":{
                String dayOfWeek=queryTimeParameters.getDayOfWeek().toString();

                switch (dayOfWeek) {
                    case "MONDAY" -> queryTimeParameters=queryTimeParameters.minusDays(0);
                    case "TUESDAY" -> queryTimeParameters=queryTimeParameters.minusDays(1);
                    case "WEDNESDAY" -> queryTimeParameters=queryTimeParameters.minusDays(2);
                    case "THURSDAY" -> queryTimeParameters=queryTimeParameters.minusDays(3);
                    case "FRIDAY" -> queryTimeParameters=queryTimeParameters.minusDays(4);
                    case "SATURDAY" -> queryTimeParameters=queryTimeParameters.minusDays(5);
                    case "SUNDAY" -> queryTimeParameters=queryTimeParameters.minusDays(6);
                }

                break;
            }
            case "month":{
                queryTimeParameters=queryTimeParameters.minusDays(queryTimeParameters.getDayOfMonth()-1);
                break;
            }
            case "year":{
                queryTimeParameters=queryTimeParameters.minusMonths(queryTimeParameters.getMonthValue()-1);
                queryTimeParameters=queryTimeParameters.minusDays(queryTimeParameters.getDayOfMonth()-1);
                break;
            }
        }

        result= communityMapper.getCommunityDetails(community_id,pageNum*10-10,queryTimeParameters);
        return result;
    }

    @Override
    public ResponseResult<List<Posts>> getExclusiveData(long community_id) {

        return new ResponseResult<>(CommonCode.SUCCESS,communityMapper.getNews(community_id));
    }

    @Override
    public ResponseResult<List<Posts>> getStrategy(long community_id){
        return new ResponseResult<>(CommonCode.SUCCESS,communityMapper.getStrategy(community_id));
    }
}
