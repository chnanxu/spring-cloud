package com.chen.service.admin;

import com.chen.mapper.admin.AuditMapper;
import com.chen.mapper.create.CreateTempMapper;
import com.chen.mapper.create.CreateMapper;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.Posts_Takeoff;
import com.chen.pojo.page.Posts_Temp;
import com.chen.repository.MongoDataPageAble;
import com.chen.repository.create.PostRepository;
import com.chen.repository.create.PostTakeoffRepository;
import com.chen.repository.create.PostTempRepository;
import com.chen.utils.result.AdminCode;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService{

    private final PostRepository postRepository;
    private final PostTempRepository postTempRepository;
    private final PostTakeoffRepository postTakeoffRepository;

    @Override  //回退作品
    public ResponseResult<String> refuseProject(String uid, String pid, String refuse_reason) {

        Posts_Temp temp_post=postTempRepository.findById(pid).orElse(null);

        if(temp_post!=null){
            return new ResponseResult<>(AdminCode.REFUSE_SUCCESS);
        }else{
            return new ResponseResult<>(AdminCode.REFUSE_FAILURE);
        }
    }

    @Override  //审核作品
    public String agreeProject(String uid, String pid) {
        Posts_Temp temp_item=postTempRepository.findById(pid).orElse(null);

        if(temp_item==null){
            return "作品不存在";
        }

        if(temp_item.getContent()==null){
            return "内容为空,不可以发布哦";
        }

        temp_item.setHref("/details/"+pid);
        Posts item = new Posts();
        BeanUtils.copyProperties(temp_item,item);
        postRepository.insert(item);

        postTempRepository.deleteById(pid);
        return "审核通过";


    }

    @Override  //下架作品
    public ResponseResult<String> takeoffProject(String pid,String takeoffTime,String takeoffReason) {
        try{
            Optional<Posts> post=postRepository.findById(pid);
            Posts item= post.orElse(null);
            if(item!=null){
                postRepository.deleteById(pid);
                Posts_Takeoff takeoff_item=new Posts_Takeoff();
                BeanUtils.copyProperties(item,takeoff_item);
                takeoff_item.setTakeoffTime(takeoffTime);
                takeoff_item.setTakeoffReason(takeoffReason);
                postTakeoffRepository.insert(takeoff_item);
                return new ResponseResult<>(AdminCode.TAKEOFF_SUCCESS);
            }else{
                return new ResponseResult<>(AdminCode.TAKEOFF_FAILURE);
            }

        }catch (Exception e){
            return new ResponseResult<>(AdminCode.TAKEOFF_FAILURE);
        }
    }

    @Override  //删除作品
    public ResponseResult<String> deleteProjectById(String pid,String projectType) {
        switch (projectType){
            case "未审核" -> postTempRepository.deleteById(pid);
            case "已发布" -> postRepository.deleteById(pid);
            case "已下架" -> postTakeoffRepository.deleteById(pid);
        }
        return new ResponseResult<>(AdminCode.DELETE_SUCCESS);
    }

    @Override //恢复作品
    public ResponseResult<String> reCoverProject(String pid) {
        Posts_Temp temp_item=postTakeoffRepository.findById(pid).orElse(null);
        if(temp_item!=null){
            Posts item=new Posts();
            BeanUtils.copyProperties(temp_item,item);
            postRepository.insert(item);
            postTakeoffRepository.deleteById(pid);
            return new ResponseResult<>(AdminCode.RECOVER_SUCCESS);
        }else{
            return new ResponseResult<>(AdminCode.RECOVER_FAILURE);
        }

    }

    @Override
    public ResponseResult<List<Posts_Temp>> getTempProject(int pageNum, int pageSize, String sortField, String sortKeywords) {
        MongoDataPageAble pageable=new MongoDataPageAble(pageNum,pageSize,Sort.by(Sort.Direction.DESC,sortField));
        Page<Posts_Temp> pageableDetail = switch (sortField) {
            case "createTime" -> postTempRepository.findByIsOKOrderByCreateTimeDesc(1,pageable);
            case "readTimes" -> postTempRepository.findByIsOKOrderByReadTimesDesc(1,pageable);
            case "uid" -> postTempRepository.findByIsOKAndUidOrderByCreateTimeDesc(1,sortKeywords, pageable);
            default -> null;
        };
        if(pageableDetail!=null){
            return new ResponseResult<>(CommonCode.SUCCESS,pageableDetail.getContent());
        }else{
            return new ResponseResult<>(CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult<List<Posts>> getProject(int pageNum, int pageSize, String sortField, String sortKeywords) {
        MongoDataPageAble pageable=new MongoDataPageAble(pageNum,pageSize,Sort.by(Sort.Direction.DESC,sortField));
        Page<Posts> pageableDetail=switch (sortField){
            case "createTime" -> postRepository.findByOrderByCreateTimeDesc(pageable);
            case "readTimes" -> postRepository.findByOrderByReadTimesDesc(pageable);
            case "uid" -> postRepository.findByUidOrderByCreateTimeDesc(sortKeywords,pageable);
            default -> null;
        };
        if (pageableDetail != null) {
            return new ResponseResult<>(CommonCode.SUCCESS,pageableDetail.getContent());
        }else{
            return new ResponseResult<>(CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult<List<Posts_Takeoff>> getTakeoffProject(int pageNum, int pageSize, String sortField, String sortKeywords) {

        MongoDataPageAble pageable=new MongoDataPageAble(pageNum,pageSize,Sort.by(Sort.Direction.DESC,sortField));
        Page<Posts_Takeoff> pageableDetail = switch (sortField) {
            case "takeoffTime" -> postTakeoffRepository.findByOrderByTakeoffTimeDesc(pageable);
            case "readTimes" -> postTakeoffRepository.findByOrderByReadTimesDesc(pageable);
            case "uid" -> postTakeoffRepository.findByUidOrderByCreateTimeDesc(sortKeywords, pageable);
            default -> null;
        };
        if(pageableDetail!=null){
            return new ResponseResult<>(CommonCode.SUCCESS,pageableDetail.getContent());
        }else{
            return new ResponseResult<>(CommonCode.FAIL);
        }
    }
}
