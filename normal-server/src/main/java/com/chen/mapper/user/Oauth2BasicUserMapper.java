package com.chen.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Mapper
@Repository
public interface Oauth2BasicUserMapper extends BaseMapper<User> {


}
