package com.chen.mapper.create;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface CreateTempMapper extends BaseMapper<Item_Details_Temp> {

    Item_Details_Temp findTempProjectByPid(String pid); //获取临时作品

    void updateTempProject(Item_Details_Temp temp_item);//更新待审核作品

    @Select("select * from item_details_temp where uid=#{uid} and (isOK=1 or isOK=-1) order by create_time desc LIMIT ${pageNumber},16")
    List<Item_Details_Temp> getMyProjectByNoAgree(String uid, int pageNumber);    //获取我的待审核作品

    @Select("select * from item_details_temp where uid=#{uid} and isOK=0 order by create_time desc LIMIT ${pageNumber},16")
    List<Item_Details_Temp> getMyProjectByDraft(String uid,int pageNumber);   //获取我的草稿

    @Select("select * from item_details_temp where uid=#{uid} and isOK=-2 order by create_time desc LIMIT ${pageNumber},16")
    List<Item_Details> getMyProjectTakeoff(String uid, int pageNumber); //获取已下架作品

    @Update("update item_details_temp set isOK=1 where pid=#{pid}")
    void recoverProject(long pid);

}
