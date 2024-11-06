package com.chen.service;

import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Item_Details;
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

    List<Item_Details> getCommunityDetailsBySortType(long community_id,int pageNum,String sortType);

    ResponseResult<List<Item_Details>> getExclusiveData(long community_id);

    ResponseResult<List<Item_Details>> getStrategy(long community_id);

}
