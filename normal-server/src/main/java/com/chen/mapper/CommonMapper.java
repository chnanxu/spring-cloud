package com.chen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface CommonMapper extends BaseMapper<Object> {

    @Select("select uid,uname,user_img,level,email from users where uid=#{uid}")
    Map<String,Object> getUserBaseInfo(String uid);

}
