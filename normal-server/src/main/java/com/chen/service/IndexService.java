package com.chen.service;


import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.HotTag;
import com.chen.pojo.page.Posts;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface IndexService {
    List<String> getHeaderItem();

    List<String> getLeftNavbar();

    List<All_Type> getTypeList();

    ResponseResult<List<HotTag>> getHotTag(String keywords, int pageNumber);

    ResponseResult<List<Posts>> getIndex(Integer typeId,Integer pageNumber,Integer pageSize, String articleType);

    ResponseResult<Map<Integer,List<Posts>>> getAnnouncement(String announcementCommunitySortType, String announcementSortType);

    ResponseResult<Map<String,List<Posts>>> getAnnouncementByCommunityId(Integer community_id, String announcementSortType);

    List<String> findUserItem();

    List<String> finCreateLeftItem();

    List<String> searchTempList(String text);

    List<Posts> getSearchDetails(String keywords, String articleType, int pageNum);
}
