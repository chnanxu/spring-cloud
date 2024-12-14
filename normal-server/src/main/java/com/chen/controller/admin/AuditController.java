package com.chen.controller.admin;

import com.chen.mapper.AdminMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.service.AdminService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@PreAuthorize("hasAnyAuthority('system:audit','system:root')")
@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AdminService adminService;

    private final AdminMapper adminMapper;

    @PostMapping("/agreeProject/{uid}/{pid}")    //审核通过
    public ResponseResult<String> agreeProject(@PathVariable String uid, @PathVariable long pid){

        String message=adminService.agreeProject(uid,pid);

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/refuseProject/{uid}/{pid}/{refuse_reason}") //回退作品
    public ResponseResult<String> refuseProject(@PathVariable String uid, @PathVariable long pid, @PathVariable String refuse_reason){

        return new ResponseResult<>(CommonCode.SUCCESS, adminService.refuseProject(uid,pid,refuse_reason));
    }

    @PostMapping("/takeoffProject/{pid}")   //下架作品
    public ResponseResult<String> takeoffProject(@PathVariable long pid){

        int code=adminService.takeoffProject(pid);
        String message="";
        if(code==0){
            message="下架成功";
        }else{
            message="下架失败";
        }

        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/deleteProject/{pid}")     //删除作品
    public ResponseResult<String> deleteProject(@PathVariable long pid){
        int code= adminMapper.deleteProjectById(pid);
        String message="";
        if(code==0){
            message="删除成功";
        }else{
            message="删除失败";
        }
        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }



    @GetMapping("/getTempProject/{pageNum}")    //获取待审核作品
    public ResponseResult<List<Item_Details_Temp>> getTempProject(@PathVariable int pageNum){

        List<Item_Details_Temp> tempData=adminService.getTempProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,tempData);
    }
    @GetMapping("/getProject/{pageNum}")        //获取已发布作品
    public ResponseResult<List<Item_Details>> getProject(@PathVariable int pageNum){

        List<Item_Details> result= adminService.getProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }


    @GetMapping("/getTakeoffProject/{pageNum}") //获取已下架作品
    public ResponseResult<List<Item_Details>> getTakeoffProject(@PathVariable int pageNum){

        List<Item_Details> result=adminService.getTakeoffProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @PostMapping("/reCoverProject/{pid}")  //
    public ResponseResult<String> reCoverProject(@PathVariable long pid){

        adminMapper.reCoverProject(pid);

        return new ResponseResult<>(CommonCode.SUCCESS,"success");
    }



}
