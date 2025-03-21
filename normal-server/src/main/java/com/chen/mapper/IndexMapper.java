package com.chen.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.HotTag;
import com.chen.pojo.page.Posts;
import com.chen.pojo.user.UserPersonalize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Mapper
@Repository
@JsonSerialize
public interface IndexMapper extends BaseMapper {
    List<String> getHeaderItem();

    List<String> getLeftNavbar();

    List<HotTag> getHotTag(int pageNumber);

    List<HotTag> getHotTagByKeywords(String keywords,int pageNumber);

    List<All_Type> getTypeList();

    List<Posts> findIndexByType_id(int type_id, String articleType);

    List<Posts> findIndexByRandom(int pageNum, String articleType);

    Collection<Posts> findIndexByPercent(int type_id, long number, String articleType);

    List<UserPersonalize> getUserPersonalize(String uid);

    List<Posts> getAnnounce(Integer community_id, LocalDate query_date);

    List<String> findUserItem();

    List<String> findCreateLeftItem();

    List<String> searchByText(String text);

    List<Posts> getSearchDetailsByKeywords(String keywords, String articleType, int pageNum);

    List<Community> getHotCommunityBySort(String sort_type);

}
