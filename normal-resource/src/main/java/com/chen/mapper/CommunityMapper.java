package com.chen.mapper;


import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Item_Details;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommunityMapper {
    List<Community> getCommunity();

    List<CommunityLeftNav> getCommunityLeftNav(long community_id);

    long getCommunityIdByName(String community_name);

    List<Map> getTotalHotCommunity();

    List<CommunityModule> getCommunityModule(long community_id);

    List<Item_Details> getCommunityDetails(long community_id, int pageNum, LocalDate queryTimeParameters);

    List<Item_Details> getNews(long community_id);

    List<Item_Details> getStrategy(long community_id);

}
