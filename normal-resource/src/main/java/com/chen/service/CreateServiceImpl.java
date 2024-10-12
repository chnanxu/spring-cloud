package com.chen.service;

import com.chen.mapper.CreateMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateServiceImpl implements CreateService{

    private final CreateMapper createMapper;

    private final UserDetailService userDetailService;

    private final RedisCache redisCache;

    @Override
    public List<Map> getCommunityListByQueryType(String queryType,int pageNum) {

        List<Map> result;
        if(queryType.equals("first")){

            result=createMapper.getCommunityList();
        }else{
            result=createMapper.getCommunityListByType(queryType,pageNum);
        }

        return result;
    }

    @Override     //上传封面实现
    public String newCoverImg(String create_id, MultipartFile file, String uid){

        String id=redisCache.getCacheObject(uid+"create_id:");

        if(id==null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            id=sdf.format(System.currentTimeMillis());
            redisCache.setCacheObject(uid+"create_id:",id);
        }
        String fileName=file.getOriginalFilename();


        String path="D:\\developTools\\Workspace\\static\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;
        String newFileName="cover_img.avif";

        return getString(create_id, file, uid, id, path, newFileName);
    }

    @Override     //上传内容图片实现
    public String newProjectImg(String create_id, String img_id, MultipartFile file, String uid) {

        String id=redisCache.getCacheObject(uid+"create_id:");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");

        if(id==null){

            id=sdf.format(System.currentTimeMillis());
            redisCache.setCacheObject(uid+"create_id:",id);
        }

        String fileName=file.getOriginalFilename();

        String path="D:\\developTools\\Workspace\\static\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

        String newFileName="content_"+img_id+".avif";

        return getString(create_id, file, uid, id, path, newFileName);
    }

    //上传本地文件方法
    private String getString(String create_id, MultipartFile file, String uid, String id, String path, String newFileName) {
        File f=new File(path);
        if(!f.exists()){
            f.mkdirs();
        }

        String url="images/user_data/"+uid+"/project_data/"+create_id+"/"+id+"/"+newFileName;

        try {
            file.transferTo(new File(path+"\\"+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "异常情况";
        }

        return url;
    }

    @Override   //上传新作品实现
    public String newProject(Item_Details_Temp temp_item, String uid) {

        try{
            String id=redisCache.getCacheObject(uid+"create_id:");
            temp_item.setPid(id);

            if(temp_item.getDetail_type().equals("article") && SensitiveWordHelper.contains(temp_item.getContent())){
                return "内容含有敏感词";
            }

            createMapper.createNewProject(temp_item);

            redisCache.deleteObject(uid+"create_id:");

        }catch(Exception e){
            e.printStackTrace();
            return "异常情况";
        }

        return "上传成功";

    }

    @Override   //上传视频实现
    public String uploadVideo(String create_id, MultipartFile video, String uid) {

        String id=redisCache.getCacheObject(uid+"create_id:");

        if(id==null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            id=sdf.format(System.currentTimeMillis());
            redisCache.setCacheObject(uid+"create_id:",id);
        }
        String fileName=video.getOriginalFilename();

        String path="D:\\developTools\\Workspace\\static\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

        String newFileName="content_video"+fileName.substring(fileName.indexOf("."));

        return getString(create_id,video,uid,id,path,newFileName);
    }

    @Override
    public int getMyProjectCount(String uid,String sortType){
        if(sortType.equals("time")||sortType.equals("hot")){
          return  createMapper.getMyProjectCount(uid);
        }else{
          return  createMapper.getMyTempProjectCount(uid,sortType.equals("waitAgree") ? -1 : 0);
        }
    }

    @Override
    public List<Item_Details> getMyProject(String uid,String sortType,int pageNumber) {
        if(sortType.equals("time")){
            return createMapper.getMyProjectByTime(uid,pageNumber);
        } else if (sortType.equals("hot")) {
            return createMapper.getMyProjectByHot(uid,pageNumber);
        }
        return null;
    }

    @Override
    public List<Item_Details_Temp> getMyProjectTemp(String uid,String sortType,int pageNumber){
        if(sortType.equals("waitAgree")){
            return createMapper.getMyProjectByNoAgree(uid,pageNumber);
        }else if(sortType.equals("draft")){
            return createMapper.getMyProjectByDraft(uid,pageNumber);
        }
        return null;
    }

    @Override
    public String updateCoverImg(long pid,MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        String path="";
        String url=createMapper.getMyProjectCoverImgSrc(pid);
//        if(url==null){
//            path="D:\\Workspace\\static\\images\\user_data\\"+user.getUid()+"\\project_data"
//        }

         path="D:\\developTools\\Workspace\\static\\"+url.substring(0,url.indexOf("cover"));

        String newFileName="cover_img"+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

        try{
            file.transferTo(new File(path+"\\"+newFileName));
        }catch (Exception e){
            e.printStackTrace();
            return "异常情况";
        }

        return url;
    }


}
