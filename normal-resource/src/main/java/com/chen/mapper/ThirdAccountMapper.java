package com.chen.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.user.Oauth2ThirdAccount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ThirdAccountMapper extends BaseMapper<Oauth2ThirdAccount> {

}
