package com.chen.mapper;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CreateMapper {
    List<Map> getCommunityList();

    List<Map> getCommunityListByType(String type_name,int pageNum);

    void createNewProject(Item_Details_Temp temp_item);  //新建作品

    int getMyProjectCount(String uid);

    int getMyTempProjectCount(String uid,int isOK);

    List<Item_Details> getMyProjectByTime(String uid,int pageNumber);    //获取我的作品按时间排序

    List<Item_Details> getMyProjectByHot(String uid,int pageNumber);    //获取我的作品按热度排序

    List<Item_Details_Temp> getMyProjectByNoAgree(String uid,int pageNumber);    //获取我的待审核作品

    List<Item_Details_Temp> getMyProjectByDraft(String uid,int pageNumber);   //获取我的草稿

    String getMyProjectCoverImgSrc(long pid);
}
