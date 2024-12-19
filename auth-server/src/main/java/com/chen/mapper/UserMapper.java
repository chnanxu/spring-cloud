package com.chen.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    int registerByEmail(User user);  //注册用户

    int registerByPhone(User user);   //根据手机号注册用户

    void createUser(User user);//创建用户

    User findByName(String username);  //根据昵称查找

    User findByUid(String uid);  //根据uid查找

    @Select("select * from users where email=#{email}")
    User findByEmail(String email);//根据email查找

    int syncUserInfo(User userData);  //同步用户信息


}
