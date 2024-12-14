package com.chen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Mapper
@Repository
public interface Oauth2BasicUserMapper extends BaseMapper<User> {

    @Select("select uid,uname,user_img,level from users where uid=#{uid}")
    Map<String,Object> getUserBaseInfo(String uid);

}
