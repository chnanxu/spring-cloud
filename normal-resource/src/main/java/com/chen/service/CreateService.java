package com.chen.service;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface CreateService {
    List<Map> getCommunityListByQueryType(String queryType, int pageNum);


    String newCoverImg(String create_id, MultipartFile file, String uid);

    String newProjectImg(String create_id,String img_id,MultipartFile file,String uid);

    String newProject(Item_Details_Temp temp_item, String uid);

    String uploadVideo(String create_id,MultipartFile video,String uid);

    int getMyProjectCount(String uid,String sortType);

    List<Item_Details> getMyProject(String uid,String sortType,int PageNumber);

    List<Item_Details_Temp> getMyProjectTemp(String uid,String sortType,int pageNumber);

    String updateCoverImg(long pid,MultipartFile file);

}
