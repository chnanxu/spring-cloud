package com.chen.mapper;



import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.pojo.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Insert("insert into user_role values (#{uid},1,'用户')")
    void createUser(User user);//创建用户

    @Select("select * from users where username=#{username} or phone=#{username} or email=#{username}")
    User findByName(String username);  //根据昵称查找

    @Select("select * from users where uid=#{uid}")
    User findByUid(String uid);  //根据uid查找

    @Select("select * from users where email=#{email}")
    User findByEmail(String email);//根据email查找

    @Update(" update users set email=#{email},phone=#{phone},sex=#{sex},sourceFrom=#{sourceFrom},location=#{location},level=#{level},exp_point=#{exp_point} where uid=#{uid}")
    int syncUserInfo(User userData);  //同步用户信息


}
