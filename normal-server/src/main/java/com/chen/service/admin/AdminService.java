package com.chen.service.admin;


import com.chen.pojo.admin.Admin_Left_Navbar;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.UserRole;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface AdminService {

    ResponseResult<List<Admin_Left_Navbar>> getAdminLeftNavbar();



    String createCommunity(Community community);

    String updateCommunityCoverImg(String community_id,MultipartFile file);

    Community updateCommunity(Community community);

    ResponseResult<UserRole> updateUserRole(UserRole userRole);

    ResponseResult<List<ReportItem>> getReportList(int pageNum);
}
