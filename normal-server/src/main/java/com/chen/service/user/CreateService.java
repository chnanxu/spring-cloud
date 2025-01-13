package com.chen.service.user;

import com.chen.pojo.page.Posts;
import com.chen.pojo.page.Posts_Temp;

import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface CreateService {
    String newCoverImg(String create_id, MultipartFile file, String uid);

    ResponseResult<String> newProjectImg(String create_id, String img_id, MultipartFile file, String uid);

    ResponseResult<List<String>> newProject(Posts_Temp temp_item, String uid);

    ResponseResult<Posts_Temp> saveTempProject(Posts_Temp temp_item, String uid);

    ResponseResult<String> updateContentImg(String pid,String img_id,MultipartFile file);

    ResponseResult reUploadProject(Posts_Temp temp_item);

    String uploadVideo(String create_id,MultipartFile video,String uid);

    ResponseResult<Integer> getMyProjectCount(String uid,String sortType);

    ResponseResult<List<Posts>> getMyProject(int pageNumber, int pageSize, String sortField, String sortKeywords);

    ResponseResult<List<Posts_Temp>> getMyProjectTemp(int pageNumber, int pageSize, String sortField, String sortKeywords);

    ResponseResult<String> updateCoverImg(String pid,MultipartFile file);

    ResponseResult<String> deleteMyProject(String pid,String postState);

    ResponseResult<String> takeoffProject(String pid);

    ResponseResult<String> reCoverProjectByPid(String pid);


}
