package com.chen.service;

import com.chen.mapper.CommunityMapper;
import com.chen.mapper.IndexMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.HotTag;
import com.chen.pojo.page.Posts;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.UserPersonalize;
import com.chen.repository.MongoDataPageAble;
import com.chen.repository.create.PostRepository;
import com.chen.service.user.UserDetailService;
import com.chen.service.user.UserService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final IndexMapper indexMapper;
    private final PostRepository postRepository;
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
    public ResponseResult<List<HotTag>> getHotTag(String keywords, int pageNumber) {

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

    public ResponseResult<List<Posts>> getIndex(Integer typeId,Integer pageNumber,Integer pageSize, String articleType){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        List<Posts> result=new ArrayList<>();
        MongoDataPageAble pageAble=new MongoDataPageAble(pageNumber,pageSize, Sort.by(Sort.Direction.DESC,"createTime"));

        if(user.getUid()==null){
            if(typeId==0){
                result=postRepository.findByOrderByCreateTimeDesc(pageAble).stream().toList(); //推荐算法待定
            }else{
                result=postRepository.findByTypeIdOrderByCreateTimeDesc(typeId,pageAble).stream().toList();
            }

        }else{
            if(typeId==0){
                List<UserPersonalize> user_personalizeTag=indexMapper.getUserPersonalize(user.getUid());

                if(user_personalizeTag==null){
                    result=postRepository.findByOrderByCreateTimeDesc(pageAble).stream().toList();//推荐算法待定
                }else{
                    int result_index=1;
                    int user_read_count=0;
                    Map<Integer,Integer> type_map=new HashMap();

                    for (UserPersonalize item:user_personalizeTag
                    ) {
                        user_read_count=user_read_count+item.getRead_times();
                    }

                    List<Posts> postsList=new LinkedList<>();

                    for (UserPersonalize item:user_personalizeTag
                    ) {
                        int percent=item.getRead_times()/user_read_count*pageSize;
                        type_map.put(item.getType_id(),percent);
                        pageAble.setPageSize(percent);
                        postsList.addAll(postRepository.findByTypeIdOrderByCreateTimeDesc(item.getType_id(),pageAble).stream().toList());
                    }
                }
            }else{
                result=postRepository.findByTypeIdOrderByCreateTimeDesc(typeId,pageAble).stream().toList();
            }

        }
        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @Override
    public ResponseResult<Map<Integer,List<Posts>>> getAnnouncement(String announcementCommunitySortType, String announcementSortType) {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        Map<Integer,List<Posts>> result=new HashMap<>();

        //作品查询按日期查询  日、周、月、年等
        LocalDate query_date=getQuery_date(announcementSortType);

        if(user.getUid()!=null){

            List<Community> guanzhuList=userService.getUserLikeCommunity(user.getUid());

            for (Community item:guanzhuList) {
               result.put(item.getCommunity_id(),indexMapper.getAnnounce(item.getCommunity_id(),query_date));
            }

        }else{

            List<Community> hotList=indexMapper.getHotCommunityBySort(announcementCommunitySortType);

            for(Community item:hotList){
                result.put(item.getCommunity_id(),indexMapper.getAnnounce(item.getCommunity_id(),query_date));
            }

        }

        return new ResponseResult<>(CommonCode.SUCCESS,result) ;

    }

    @Override
    public ResponseResult<Map<String,List<Posts>>> getAnnouncementByCommunityId(Integer community_id, String announcementSortType) {

        LocalDate query_date=getQuery_date(announcementSortType);
        Map<String,List<Posts>> result=new HashMap<>();
        List<Posts> announce=new ArrayList<>();
        if(community_id==0){
            announce=indexMapper.getAnnounce(community_id,query_date);
            result.put(communityMapper.getCommunityById(community_id).getCommunity_name(),announce);
        }else{
            announce=indexMapper.getAnnounce(community_id,query_date);

            result.put(communityMapper.getCommunityById(community_id).getCommunity_name(),announce);
        }


        return new ResponseResult<>(CommonCode.SUCCESS,result);

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
    public List<Posts> getSearchDetails(String keywords, String articleType, int pageNum) {

        if(keywords==null){
            return indexMapper.findIndexByRandom(pageNum,articleType);
        }else{
            return indexMapper.getSearchDetailsByKeywords(keywords,articleType,pageNum);
        }

    }



}
