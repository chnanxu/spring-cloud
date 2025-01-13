package com.chen.mapper.create;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.page.Posts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CreateMapper extends BaseMapper<Posts> {

    @Select("select count(*) from item_details where uid=#{uid}")
    int getMyProjectCount(String uid);   //获取作品数量

    @Select("select count(*) from item_details_temp where uid=#{uid}")
    int getMyTempProjectCount(String uid,int isOK); //获取临时作品数量

    @Select("select * from item_details where uid=#{uid} order by create_time desc limit ${pageNumber},16")
    List<Posts> getMyProjectByTime(String uid, int pageNumber);    //获取我的作品按时间排序

    @Select("select * from item_details where uid=#{uid} order by read_times desc limit ${pageNumber},16")
    List<Posts> getMyProjectByHot(String uid, int pageNumber);    //获取我的作品按热度排序

    @Select("select cover_img from item_details where pid=#{pid}")
    String getMyProjectCoverImgSrc(Long pid);

}
