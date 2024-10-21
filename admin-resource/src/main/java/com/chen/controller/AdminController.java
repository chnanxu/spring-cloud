package com.chen.controller;


import com.chen.mapper.AdminMapper;
import com.chen.pojo.admin.Admin_Left_Navbar;
import com.chen.pojo.admin.ReportItem;
import com.chen.pojo.community.Community;
import com.chen.pojo.permission.Role;
import com.chen.pojo.user.User;
import com.chen.pojo.user.UserRole;
import com.chen.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import result.CommonCode;
import result.ResponseResult;

import java.util.List;

@PreAuthorize("hasAnyAuthority('system:manager','system:root')")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminMapper adminMapper;

    private final AdminService adminService;

    @GetMapping("/getAdminLeftNavbar")
    public ResponseResult<List<Admin_Left_Navbar>> getAdminLeftNavbar(){

        return adminService.getAdminLeftNavbar();
    }
    @GetMapping("/getUser/{pageNum}")   //批量获取用户
    public ResponseResult<List<User>> getUser(@PathVariable int pageNum){

        List<User> userData=adminMapper.getUser(pageNum*14-14);

        return new ResponseResult<>(CommonCode.SUCCESS,userData);
    }

    @GetMapping("/getRole/{pageNum}")   //获取角色
    public ResponseResult<List<Role>> getRole(@PathVariable int pageNum){

        List<Role> result=adminMapper.getRole(pageNum*14-14);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @PostMapping("/updateUserRole")   //更新用户角色
    public ResponseResult<UserRole> updateUserRole(@RequestBody UserRole user){

        return adminService.updateUserRole(user);
    }


    @GetMapping("/getCommunity/{pageNum}")    //获取社区
    public ResponseResult<List<Community>> getCommunity(@PathVariable int pageNum){

        List<Community> result=adminMapper.getCommunity(pageNum*10-10);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @PostMapping("/createCommunity")   //新建社区
    public ResponseResult<String> createCommunity(@RequestBody Community community){
        String message= adminService.createCommunity(community);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/updateCommunityCoverImg/{community_id}")   //更新社区封面
    public ResponseResult<String> updateCommunityCoverImg(@PathVariable String community_id, @RequestParam("file") MultipartFile file){

        adminService.updateCommunityCoverImg(community_id,file);

        return new ResponseResult<>(CommonCode.SUCCESS,"success");
    }

    @PostMapping("/updateCommunity")        //更新社区信息
    public ResponseResult<Community> updateCommunity(@RequestBody Community community){

        community=adminService.updateCommunity(community);

        return new ResponseResult<>(CommonCode.SUCCESS,community);
    }

    @GetMapping("/getReportData/{pageNum}")  //获取举报信息
    public ResponseResult<List<ReportItem>> getReportList(@PathVariable int pageNum){
        return adminService.getReportList(pageNum*14-14);
    }

}
