package com.chen.controller.admin;

import com.chen.mapper.admin.AdminMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.service.admin.AuditService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@PreAuthorize("hasAnyAuthority('system:audit','system:root')")
@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/agreeProject")    //审核通过
    public ResponseResult<String> agreeProject(@RequestBody Map<String, Object> params){
        String uid=params.get("uid").toString();
        long pid=Long.parseLong(params.get("pid").toString());
        String message=auditService.agreeProject(uid,pid);
        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/refuseProject") //回退作品
    public ResponseResult<String> refuseProject(@RequestBody Map<String, Object> params){
        String uid=params.get("uid").toString();
        long pid=Long.parseLong(params.get("pid").toString());
        String refuse_reason=params.get("refuse_reason").toString();
        return auditService.refuseProject(uid,pid,refuse_reason);
    }

    @PostMapping("/takeoffProject")   //下架作品
    public ResponseResult<String> takeoffProject(@RequestBody Map<String,String> params){
        long pid=Long.parseLong(params.get("pid"));
        String takeoff_reason =params.get("takeoff_reason");
        return auditService.takeoffProject(pid,takeoff_reason);
    }

    @PostMapping("/deleteProject")     //删除作品
    public ResponseResult<String> deleteProject(@RequestBody Map<String,Object> params){
        long pid=Long.parseLong((String)params.get("pid"));
        return auditService.deleteProjectById(pid);
    }



    @GetMapping("/getTempProject/{pageNum}")    //获取待审核作品
    public ResponseResult<List<Item_Details_Temp>> getTempProject(@PathVariable int pageNum){

        List<Item_Details_Temp> tempData=auditService.getTempProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,tempData);
    }
    @GetMapping("/getProject/{pageNum}")        //获取已发布作品
    public ResponseResult<List<Item_Details>> getProject(@PathVariable int pageNum){

        List<Item_Details> result= auditService.getProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }


    @GetMapping("/getTakeoffProject/{pageNum}") //获取已下架作品
    public ResponseResult<List<Item_Details_Temp>> getTakeoffProject(@PathVariable int pageNum){

        List<Item_Details_Temp> result=auditService.getTakeoffProject(pageNum);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @PostMapping("/reCoverProject")  //恢复作品
    public ResponseResult<String> reCoverProject(@RequestBody Map<String, Object> params){
        long pid = Long.parseLong((String)params.get("pid"));
        return auditService.reCoverProject(pid);
    }



}
