package com.chen.service;

import com.chen.mapper.CommunityMapper;
import com.chen.mapper.IndexMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.HotTag;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.UserPersonalize;
import com.chen.utils.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import result.CommonCode;
import result.ResponseResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final IndexMapper indexMapper;
    private final CommunityMapper communityMapper;
    private final UserDetailService userDetailService;
    private final UserService userService;

    private final RedisCache redisCache;

    @Override
    public List<String> getHeaderItem() {
        return indexMapper.getHeaderItem();
    }

    @Override
    public List<All_Type> getTypeList() {
        return indexMapper.getTypeList();
    }

    @Override
    public ResponseResult<List<HotTag>> getHotTag(String keywords,int pageNumber) {

        if(keywords==null || keywords.isEmpty()){
            return new ResponseResult<>(CommonCode.SUCCESS,indexMapper.getHotTag(pageNumber*10-10));
        }else{
            return new ResponseResult<>(CommonCode.SUCCESS,indexMapper.getHotTagByKeywords(keywords,pageNumber*10-10));
        }


    }

    @Override
    public List<String> getLeftNavbar() {
        return indexMapper.getLeftNavbar();
    }

    public List<Item_Details> getIndex(int type_id,String articleType){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        List<Item_Details> result =new ArrayList<>();

        if(user.getUid()==null){
            if(type_id==0){
                result=indexMapper.findIndexByRandom(0,articleType);
            }else{
                result=indexMapper.findIndexByType_id(type_id,articleType);
            }

        }else{

            if(type_id==0){
                List<UserPersonalize> user_personalizeTag=indexMapper.getUserPersonalize(user.getUid());

                if(user_personalizeTag==null){
                    result=indexMapper.findIndexByRandom(0,articleType);
                }else{
                    int result_index=1;
                    float user_read_count=0;
                    Map type_map=new HashMap();

                    for (UserPersonalize item:user_personalizeTag
                    ) {
                        user_read_count=user_read_count+item.getRead_times();
                    }

                    for (UserPersonalize item:user_personalizeTag
                    ) {
                        type_map.put(item.getType_id(),item.getRead_times()/user_read_count*18);
                        result.addAll(indexMapper.findIndexByPercent(item.getType_id(),Math.round((item.getRead_times()/user_read_count*18)),articleType));
                    }
                }
            }else{
                result=indexMapper.findIndexByType_id(type_id,articleType);
            }

        }
        return result;
    }

    @Override
    public Map<String,List<Item_Details>> getAnnouncement(String announcementCommunitySortType,String announcementSortType) {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        Map<String,List<Item_Details>> result=new HashMap<>();

        //作品查询按日期查询  日、周、月、年等
        LocalDate query_date=getQuery_date(announcementSortType);

        if(user.getUid()!=null){

            List<Community> guanzhuList=userService.getUserLikeCommunity(user.getUid());

            for (Community item:guanzhuList) {
               result.put(item.getCommunity_name(),indexMapper.getAnnounce(item.getCommunity_id(),query_date));
            }

        }else{

            List<Community> hotList=indexMapper.getHotCommunityBySort(announcementCommunitySortType);

            for(Community item:hotList){
                result.put(item.getCommunity_name(),indexMapper.getAnnounce(item.getCommunity_id(),query_date));
            }

        }

        return result;

    }

    @Override
    public List<Item_Details> getAnnouncementByCommunityName(String community_name,String announcementSortType) {

        LocalDate query_date=getQuery_date(announcementSortType);

        return indexMapper.getAnnounce(communityMapper.getCommunityIdByName(community_name),query_date);

    }

    public LocalDate getQuery_date(String announcementSortType){
        //作品查询按日期查询  日、周、月、年等
        LocalDate query_date=LocalDate.now();

        if(announcementSortType!=null){
            switch (announcementSortType){
                case "week":
                    String dayOfWeek=query_date.getDayOfWeek().toString();
                    switch (dayOfWeek){
                        case "MONDAY":
                            query_date=query_date.minusDays(0);
                            break;
                        case "TUESDAY":
                            query_date=query_date.minusDays(1);
                            break;
                        case "WEDNESDAY":
                            query_date=query_date.minusDays(2);
                            break;
                        case "THURSDAY":
                            query_date=query_date.minusDays(3);
                            break;
                        case "FRIDAY":
                            query_date=query_date.minusDays(4);
                            break;
                        case "SATURDAY":
                            query_date=query_date.minusDays(5);
                            break;
                        case "SUNDAY":
                            query_date=query_date.minusDays(6);
                            break;
                    }
                    break;
                case "month":
                    query_date=query_date.minusDays(query_date.getDayOfMonth()-1);
                    break;
                case "quarter":
                    query_date=query_date.minusMonths(2);
                    query_date=query_date.minusDays(query_date.getDayOfMonth()-1);
                    break;
                case "year":
                    query_date=query_date.minusMonths(query_date.getMonthValue()-1);
                    query_date=query_date.minusDays(query_date.getDayOfMonth()-1);
                    break;
                case "total":
                    query_date=query_date.minusYears(99);
                    query_date=query_date.minusMonths(query_date.getMonthValue()-1);
                    query_date=query_date.minusDays(query_date.getDayOfMonth()-1);
            }

        }else{

        }
        return query_date;
    }


    @Override
    public List<String> findUserItem() {
        return indexMapper.findUserItem();
    }

    @Override
    public List<String> finCreateLeftItem() {
        return indexMapper.findCreateLeftItem();
    }

    @Override
    public List<String> searchTempList(String text){


        return indexMapper.searchByText(text);
    }


    @Override
    public List<Item_Details> getSearchDetails(String keywords,String articleType,int pageNum) {

        if(keywords==null){
            return indexMapper.findIndexByRandom(pageNum,articleType);
        }else{
            return indexMapper.getSearchDetailsByKeywords(keywords,articleType,pageNum);
        }

    }



}
