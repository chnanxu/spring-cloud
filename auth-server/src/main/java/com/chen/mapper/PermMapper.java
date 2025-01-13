package com.chen.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.permission.SysRoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermMapper extends BaseMapper<SysRoleAuthority> {

    @Select(" SELECT DISTINCT authority FROM authorities WHERE authId in ( SELECT authId FROM role_authorities WHERE roleId in ( SELECT roleId FROM user_role WHERE uid = #{uid} ))")
    List<String> getAuthority(String uid);

    @Select("select distinct roleId from user_role where uid=#{uid}")
    List<SysRoleAuthority> getRole(String uid);
}
