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

    @Select(" SELECT DISTINCT authority FROM authorities WHERE auth_id in ( SELECT auth_id FROM role_authorities WHERE role_id in ( SELECT role_id FROM user_role WHERE uid = #{uid} ))")
    List<String> getAuthority(String uid);

    @Select("select distinct role_id from user_role where uid=#{uid}")
    List<SysRoleAuthority> getRole(String uid);
}
