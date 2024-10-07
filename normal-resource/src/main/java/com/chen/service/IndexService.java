package com.chen.service;


import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.Item_Details;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IndexService {
    List<String> getHeaderItem();

    List<String> getLeftNavbar();

    List<All_Type> getTypeList();

    List<Item_Details> getIndex(int type_id,String articleType);

    Map<String,List<Item_Details>> getAnnouncement(String announcementCommunitySortType,String announcementSortType);

    List<Item_Details> getAnnouncementByCommunityName(String community_name,String announcementSortType);

    List<String> findUserItem();

    List<String> finCreateLeftItem();

    List<String> searchTempList(String text);

    List<Item_Details> getSearchDetails(String keywords,String articleType,int pageNum);
}
