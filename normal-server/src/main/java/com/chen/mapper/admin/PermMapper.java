package com.chen.mapper.admin;



import com.chen.pojo.permission.SysRoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermMapper {
    List<String> getAuthority(String uid);

    List<SysRoleAuthority> getRole(String uid);
}
