package com.chen.service;

import com.chen.mapper.AdminMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminMapper adminMapper;

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
    public String refuseProject(String uid, long pid,String refuse_reason) {
        if(adminMapper.refuseProjectById(uid,pid,refuse_reason)==1){
            return "操作成功";
        }else{
            return "操作失败";
        }
    }

    @Override
    public String agreeProject(String uid, long pid) {
        Item_Details_Temp temp_item=adminMapper.getTempProjectById(uid,pid);

        if(temp_item==null){
            return "作品不存在";
        }

        if(temp_item.getContent()==null){
            return "内容为空,不可以发布哦";
        }

        temp_item.setHref("/details/"+pid);
        int result=adminMapper.setProject(temp_item);

        if(result==1){
            adminMapper.deleteTempProject(pid);
            return "审核通过";
        }else{
            return "异常,请联系管理员";
        }


    }

    @Override
    public int takeoffProject(long pid) {
        return adminMapper.takeoffProjectById(pid);
    }

    @Override
    public List<Item_Details_Temp> getTempProject(int pageNum) {
        return adminMapper.getTempProjectList(pageNum*10-10);
    }

    @Override
    public List<Item_Details> getProject(int pageNum) {
        return adminMapper.getProjectList(pageNum*10-10);
    }

    @Override
    public List<Item_Details> getDeletedProject(int pageNum) {
        return adminMapper.getDeletedProjectList(pageNum*10-10);
    }

    @Override
    public List<Item_Details> getTakeoffProject(int pageNum) {
        return adminMapper.getTakeoffProjectList(pageNum*10-10);
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
