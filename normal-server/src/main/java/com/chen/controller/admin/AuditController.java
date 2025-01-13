package com.chen.controller.admin;

import com.chen.pojo.page.Posts;
import com.chen.pojo.page.Posts_Takeoff;
import com.chen.pojo.page.Posts_Temp;
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
        String pid=params.get("pid").toString();
        String message=auditService.agreeProject(uid,pid);
        return new ResponseResult<>(CommonCode.SUCCESS,message);
    }

    @PostMapping("/refuseProject") //回退作品
    public ResponseResult<String> refuseProject(@RequestBody Map<String, Object> params){
        String uid=params.get("uid").toString();
        String pid=params.get("pid").toString();
        String refuse_reason=params.get("refuse_reason").toString();
        return auditService.refuseProject(uid,pid,refuse_reason);
    }

    @PostMapping("/takeoffProject")   //下架作品
    public ResponseResult<String> takeoffProject(@RequestBody Map<String,String> params){
        String pid=params.get("pid");
        String takeoffReason =params.get("takeoffReason");
        String takeoffTime=params.get("takeoffTime");
        return auditService.takeoffProject(pid,takeoffTime,takeoffReason);
    }

    @PostMapping("/deleteProject")     //删除作品
    public ResponseResult<String> deleteProject(@RequestBody Map<String,Object> params){
        String pid=(String)params.get("pid");
        String projectType=(String)params.get("projectType");
        return auditService.deleteProjectById(pid,projectType);
    }



    @GetMapping("/getTempProject/{pageNum}/{pageSize}/{sortField}/{sortKeywords}")    //获取待审核作品
    public ResponseResult<List<Posts_Temp>> getTempProject(@PathVariable int pageNum,@PathVariable int pageSize,@PathVariable String sortField,@PathVariable String sortKeywords){

        return auditService.getTempProject(pageNum,pageSize,sortField,sortKeywords);

    }
    @GetMapping("/getProject/{pageNum}/{pageSize}/{sortField}/{sortKeywords}")        //获取已发布作品
    public ResponseResult<List<Posts>> getProject(@PathVariable int pageNum,@PathVariable int pageSize,@PathVariable String sortField,@PathVariable String sortKeywords){

        return auditService.getProject(pageNum,pageSize,sortField,sortKeywords);
    }


    @GetMapping("/getTakeoffProject/{pageNum}/{pageSize}/{sortField}/{sortKeywords}") //获取已下架作品
    public ResponseResult<List<Posts_Takeoff>> getTakeoffProject(@PathVariable int pageNum, @PathVariable int pageSize, @PathVariable String sortField, @PathVariable String sortKeywords){

        return auditService.getTakeoffProject(pageNum,pageSize,sortField,sortKeywords);

    }

    @PostMapping("/reCoverProject")  //恢复作品
    public ResponseResult<String> reCoverProject(@RequestBody Map<String, Object> params){
        String pid = (String)params.get("pid");
        return auditService.reCoverProject(pid);
    }



}
