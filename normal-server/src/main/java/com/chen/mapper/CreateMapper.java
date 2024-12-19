package com.chen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CreateMapper extends BaseMapper<Item_Details> {


    void createNewProject(Item_Details_Temp temp_item);  //新建作品

    int getMyProjectCount(String uid);   //获取作品数量

    int getMyTempProjectCount(String uid,int isOK); //获取临时作品数量

    List<Item_Details> getMyProjectByTime(String uid,int pageNumber);    //获取我的作品按时间排序

    List<Item_Details> getMyProjectByHot(String uid,int pageNumber);    //获取我的作品按热度排序

    String getMyProjectCoverImgSrc(String pid);

    int takeoffProject(String pid);  //下架作品

}
