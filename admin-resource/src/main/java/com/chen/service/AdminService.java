package com.chen.service;


import com.chen.pojo.admin.ReportItem;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.User;
import com.chen.pojo.user.UserRole;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface AdminService {
    String refuseProject(String uid,long pid,String refuse_reason);
    String agreeProject(String uid,long pid);

    int takeoffProject(long pid);
    List<Item_Details_Temp> getTempProject(int pageNum);
    List<Item_Details> getProject(int pageNum);
    List<Item_Details> getDeletedProject(int pageNum);
    List<Item_Details> getTakeoffProject(int pageNum);

    String createCommunity(Community community);

    String updateCommunityCoverImg(String community_id,MultipartFile file);
    Community updateCommunity(Community community);

    ResponseResult<UserRole> updateUserRole(UserRole userRole);

    ResponseResult<List<ReportItem>> getReportList(int pageNum);
}
