package com.chen.service.admin;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuditService {

    ResponseResult<String> refuseProject(String uid, long pid, String refuse_reason);

    String agreeProject(String uid,long pid);

    ResponseResult<String> takeoffProject(long pid,String takeoff_reason);

    ResponseResult<String> deleteProjectById(long pid);

    ResponseResult<String> reCoverProject(long pid);

    List<Item_Details_Temp> getTempProject(int pageNum);

    List<Item_Details> getProject(int pageNum);

    List<Item_Details_Temp> getTakeoffProject(int pageNum);
}
