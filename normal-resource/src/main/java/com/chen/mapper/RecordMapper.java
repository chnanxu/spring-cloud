package com.chen.mapper;


import com.chen.pojo.page.Item_Details;
import com.chen.pojo.user.Oauth2UserinfoResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecordMapper {

    Oauth2UserinfoResult getUserInfo(String uid);

    List<Item_Details> getUserProject(String uid);

}
