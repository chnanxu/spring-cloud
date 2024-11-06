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

    Item_Details findProjectByPid(String pid);  //根据pid获取作品

    void createNewProject(Item_Details_Temp temp_item);  //新建作品

    void updateTempProject(Item_Details_Temp temp_item);//更新待审核作品

    int getMyProjectCount(String uid);   //获取作品数量

    int getMyTempProjectCount(String uid,int isOK); //获取临时作品数量

    List<Item_Details> getMyProjectByTime(String uid,int pageNumber);    //获取我的作品按时间排序

    List<Item_Details> getMyProjectByHot(String uid,int pageNumber);    //获取我的作品按热度排序

    List<Item_Details_Temp> getMyProjectByNoAgree(String uid,int pageNumber);    //获取我的待审核作品

    List<Item_Details_Temp> getMyProjectByDraft(String uid,int pageNumber);   //获取我的草稿

    List<Item_Details> getMyProjectTakeoff(String uid,int pageNumber); //获取已下架作品

    String getMyProjectCoverImgSrc(String pid);

    int takeoffProject(String pid);  //下架作品

    void deleteMyProject(String pid,String uid);//删除作品,不可逆
}
