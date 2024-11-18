package com.chen.service;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;

import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Service
public interface CreateService {

    String newCoverImg(String create_id, MultipartFile file, String uid);

    ResponseResult<String> newProjectImg(String create_id, String img_id, MultipartFile file, String uid);

    ResponseResult<List<String>> newProject(Item_Details_Temp temp_item, String uid);

    ResponseResult<String> updateContentImg(String pid,String img_id,MultipartFile file);

    ResponseResult reUploadProject(Item_Details_Temp temp_item);

    String uploadVideo(String create_id,MultipartFile video,String uid);

    Integer getMyProjectCount(String uid,String sortType);

    ResponseResult<List<Item_Details>> getMyProject(String uid,String sortType,int PageNumber);

    ResponseResult<List<Item_Details_Temp>> getMyProjectTemp(String uid,String sortType,int pageNumber);

    String updateCoverImg(String pid,MultipartFile file);

    ResponseResult<String> deleteMyProject(String pid);

    ResponseResult<String> takeoffProject(String pid);

    ResponseResult<String> reCoverProjectByPid(String pid);


}
