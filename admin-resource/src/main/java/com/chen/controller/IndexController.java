package com.chen.controller;

import com.chen.pojo.admin.Admin_Left_Navbar;
import com.chen.service.AdminService;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasAnyAuthority('system:manager','system:root')")
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final AdminService adminService;

    @GetMapping("/getAdminLeftNavbar")
    public ResponseResult<List<Admin_Left_Navbar>> getAdminLeftNavbar(){

        return adminService.getAdminLeftNavbar();
    }
}
