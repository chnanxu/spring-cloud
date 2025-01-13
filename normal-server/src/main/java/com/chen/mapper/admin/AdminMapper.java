package com.chen.mapper.admin;


import com.chen.pojo.admin.Admin_Left_Navbar;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.permission.Role;
import com.chen.pojo.user.User;
import com.chen.pojo.user.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdminMapper {

    List<Admin_Left_Navbar> getLeftNavbar();

    List<Admin_Left_Navbar> getSonLeftNavbar(int id);

    List<User> getUser(int pageNum);

    List<Role> getRole(int pageNum);

    List<Community> getCommunity(int pageNum);

    List<Posts> getTakeoffProjectList(int pageNum);

    Community updateCommunity(Community community);

    int updateUserRole(UserRole userRole);

    List<ReportItem> getReportListByPageNum(int pageNum);
}
