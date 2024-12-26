package com.chen.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuditMapper extends BaseMapper<Item_Details_Temp> {

    //获取待审核数据
    @Select("select * from item_details_temp where isOK=1 order by create_time ASC limit ${pageNum},15")
    List<Item_Details_Temp> getTempProjectList(int pageNum);

    //获取单个临时数据
    @Select("select DISTINCT * from item_details_temp where uid=#{uid} and pid=#{pid}")
    Item_Details_Temp getTempProjectById(String uid,long pid);

    //获取已发布作品列表
    @Select("select * from item_details order by create_time desc limit ${pageNum},15")
    List<Item_Details> getProjectList(int pageNum);

    //获取已下架作品列表
    @Select("select * from item_details_temp where isOK=-2 order by create_time ")
    List<Item_Details_Temp> getTakeOffList(int pageNum);

    //恢复作品
    @Update("update item_details_temp set isOK=1 where pid=#{pid}")
    int reCoverProject(long pid);

    //回退作品
    @Update("update item_details_temp set isOK=-1,refuse_reason=#{refuse_reason} where pid=#{pid} and uid=#{uid}")
    int refuseProjectById(String uid,long pid,String refuse_reason);


}
