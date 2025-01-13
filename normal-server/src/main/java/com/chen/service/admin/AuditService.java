package com.chen.service.admin;

import com.chen.pojo.page.Posts;
import com.chen.pojo.page.Posts_Takeoff;
import com.chen.pojo.page.Posts_Temp;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuditService {

    ResponseResult<String> refuseProject(String uid, String pid, String refuse_reason);

    String agreeProject(String uid,String pid);

    ResponseResult<String> takeoffProject(String pid,String takeoffTime,String takeoffReason);

    ResponseResult<String> deleteProjectById(String pid,String projectType);

    ResponseResult<String> reCoverProject(String pid);

    ResponseResult<List<Posts_Temp>> getTempProject(int pageNum, int pageSize, String sortField, String sortKeywords);

    ResponseResult<List<Posts>> getProject(int pageNum, int pageSize, String sortField, String sortKeywords);

    ResponseResult<List<Posts_Takeoff>> getTakeoffProject(int pageNum, int pageSize, String sortField, String sortKeywords);
}
