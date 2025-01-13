package com.chen.service;

import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Posts;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CommunityService {

    ResponseResult<List<Map>> getCommunityListByQueryType(Integer queryType, int pageNum);


    List<Community> getCommunity();

    List<Map> getTotalHotCommunity();

    List<CommunityLeftNav> getCommunityLeftNav(long community_id);

    ResponseResult<List<CommunityModule>> getCommunityModule(long community_id);

    List<Posts> getCommunityDetailsBySortType(long community_id, int pageNum, String sortType);

    ResponseResult<List<Posts>> getExclusiveData(long community_id);

    ResponseResult<List<Posts>> getStrategy(long community_id);

}
