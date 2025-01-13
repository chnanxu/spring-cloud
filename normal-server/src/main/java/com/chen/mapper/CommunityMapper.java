package com.chen.mapper;


import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Posts;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommunityMapper {

    List<Map> getCommunityList(int pageNum);   //获取社区列表

    List<Map> getCommunityListByType(Integer type_id,int pageNum);   //根据类型获取社区列表

    List<Community> getCommunity();

    List<CommunityLeftNav> getCommunityLeftNav(long community_id);

    Community getCommunityById(Integer community_id);

    long getCommunityIdByName(String community_name);

    List<Map> getTotalHotCommunity();

    List<CommunityModule> getCommunityModule(long community_id);

    List<Posts> getCommunityDetails(long community_id, int pageNum, LocalDate queryTimeParameters);

    List<Posts> getNews(long community_id);

    List<Posts> getStrategy(long community_id);

}
