package com.chen.service.admin;

import com.chen.mapper.admin.AuditMapper;
import com.chen.mapper.create.CreateTempMapper;
import com.chen.mapper.user.CreateMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.utils.result.AdminCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService{

    private final AuditMapper auditMapper;
    private final CreateMapper createMapper;
    private final CreateTempMapper createTempMapper;

    @Override
    public ResponseResult<String> refuseProject(String uid, long pid, String refuse_reason) {
        if(auditMapper.refuseProjectById(uid,pid,refuse_reason)==1){
            return new ResponseResult<>(AdminCode.REFUSE_SUCCESS);
        }else{
            return new ResponseResult<>(AdminCode.REFUSE_FAILURE);
        }
    }

    @Override
    public String agreeProject(String uid, long pid) {
        Item_Details_Temp temp_item=auditMapper.getTempProjectById(uid,pid);

        if(temp_item==null){
            return "作品不存在";
        }

        if(temp_item.getContent()==null){
            return "内容为空,不可以发布哦";
        }
        temp_item.setHref("/details/"+pid);
        int result=createMapper.insert(temp_item);

        if(result==1){
            createTempMapper.deleteById(pid);
            return "审核通过";
        }else{
            return "异常,请联系管理员";
        }


    }

    @Override
    public ResponseResult<String> takeoffProject(long pid,String takeoff_reason) {
        try{
            Item_Details item=createMapper.selectById(pid);
            createMapper.deleteById(pid);
            Item_Details_Temp temp_item=new Item_Details_Temp();
            BeanUtils.copyProperties(item,temp_item);
            temp_item.setIsOK(-2);
            createTempMapper.insert(temp_item);
            return new ResponseResult<>(AdminCode.TAKEOFF_SUCCESS);
        }catch (Exception e){
            return new ResponseResult<>(AdminCode.TAKEOFF_FAILURE);
        }
    }

    @Override
    public ResponseResult<String> deleteProjectById(long pid) {
        if(createMapper.deleteById(pid)==1){
            return new ResponseResult<>(AdminCode.DELETE_SUCCESS);
        }else{
            return new ResponseResult<>(AdminCode.DELETE_FAILURE);
        }
    }

    @Override
    public ResponseResult<String> reCoverProject(long pid) {
        Item_Details_Temp temp_item=createTempMapper.selectById(pid);

        if(createMapper.insert(temp_item)==1){
            createTempMapper.deleteById(pid);
            return new ResponseResult<>(AdminCode.RECOVER_SUCCESS);

        }else{
            return new ResponseResult<>(AdminCode.RECOVER_FAILURE);
        }

    }

    @Override
    public List<Item_Details_Temp> getTempProject(int pageNum) {
        return auditMapper.getTempProjectList(pageNum*10-10);
    }

    @Override
    public List<Item_Details> getProject(int pageNum) {
        return auditMapper.getProjectList(pageNum*10-10);
    }

    @Override
    public List<Item_Details_Temp> getTakeoffProject(int pageNum) {
        return auditMapper.getTakeOffList(pageNum*10-10);
    }
}
