package com.chen.service.admin;

import com.chen.mapper.admin.AdminMapper;
import com.chen.mapper.user.CreateMapper;
import com.chen.pojo.admin.Admin_Left_Navbar;
import com.chen.pojo.community.Community;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.page.ReportItem;
import com.chen.pojo.user.UserRole;

import com.chen.utils.result.AdminCode;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminMapper adminMapper;

    private final CreateMapper createMapper;

    @Override
    public ResponseResult<List<Admin_Left_Navbar>> getAdminLeftNavbar() {

        List<Admin_Left_Navbar> result=adminMapper.getLeftNavbar();

        for(Admin_Left_Navbar item:result){
            item.setSonList(adminMapper.getSonLeftNavbar(item.getId()));
        }


        if(result==null){
            return new ResponseResult<>(AdminCode.GET_SUCCESS,null);
        }

        return new ResponseResult<>(AdminCode.GET_SUCCESS,result);
    }

    @Override
    public String createCommunity(Community community) {

        System.out.println(community.getCover_file());

        return null;
    }

    @Override
    public Community updateCommunity(Community community) {

        return adminMapper.updateCommunity(community);
    }

    @Override
    public ResponseResult<UserRole> updateUserRole(UserRole userRole) {
        return new ResponseResult<>(adminMapper.updateUserRole(userRole)==1 ? CommonCode.SUCCESS:CommonCode.FAIL);
    }

    @Override
    public String updateCommunityCoverImg(String community_id,MultipartFile file){

        String file_name=file.getOriginalFilename();

        String path="D:\\developTools\\Workspace\\static\\images\\web_data\\community\\"+community_id;

        String newCoverFileName="cover_img.png";

        try{
            file.transferTo(new File(path+"\\"+newCoverFileName));
        }catch (IOException e){
            e.printStackTrace();
        }

        return "success";
    }

    @Override
    public ResponseResult<List<ReportItem>> getReportList(int pageNum){
       return new ResponseResult<>(CommonCode.SUCCESS,adminMapper.getReportListByPageNum(pageNum));
    }

}
