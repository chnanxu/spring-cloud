package com.chen.service;

import com.chen.mapper.CreateMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.CreateCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.CustomSecurityProperties;
import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.bs.SensitiveWordContext;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    private final CustomSecurityProperties customSecurityProperties;

    private final SensitiveWordBs sensitiveWordBs;

    @Override     //上传封面实现
    public String newCoverImg(String create_id, MultipartFile file, String uid){

        String id=redisCache.getCacheObject(uid+"create_id:");

        if(id==null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            id=sdf.format(System.currentTimeMillis());
            redisCache.setCacheObject(uid+"create_id:",id);
        }
        String path= customSecurityProperties.getStaticPath()+"images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;
        String newFileName="cover_img.avif";

        return getString(create_id, file, uid, id, path, newFileName);
    }

    @Override     //上传内容图片实现
    public ResponseResult<String> newProjectImg(String create_id, String img_id, MultipartFile file, String uid) {

        String id=redisCache.getCacheObject(uid+"create_id:");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");

        if(id==null){

            id=sdf.format(System.currentTimeMillis());
            redisCache.setCacheObject(uid+"create_id:",id);

        }

        String fileName=file.getOriginalFilename();

        String path= customSecurityProperties.getStaticPath()+"images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

        String newFileName="content_"+img_id+".avif";

        String resultUrl=getString(create_id, file, uid, id, path, newFileName);

        return new ResponseResult<>(CommonCode.SUCCESS,resultUrl);
    }

    //上传本地文件方法
    public String getString(String create_id, MultipartFile file, String uid, String id, String path, String newFileName) {
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
    public ResponseResult<List<String>> newProject(Item_Details_Temp temp_item, String uid) {

        try{
            String id=redisCache.getCacheObject(uid+"create_id:");
            temp_item.setPid(id);

//            if(temp_item.getDetail_type().equals("article") && sensitiveWordBs.contains(temp_item.getContent())){
//                List<String> sensitiveWordList= sensitiveWordBs.findAll(temp_item.getContent());
//                return new ResponseResult<>(CreateCode.CONTAIN_SENSITIVEWORD,sensitiveWordList);
//            }
            createMapper.createNewProject(temp_item);

            redisCache.deleteObject(uid+"create_id:");

        }catch(Exception e){
            e.printStackTrace();
            return new ResponseResult<>(CommonCode.FAIL);
        }

        return new ResponseResult<>(CreateCode.CREATE_SUCCESS);

    }

    @Override   //编辑已发布内容图片
    public ResponseResult<String> updateContentImg(String pid, String img_id,MultipartFile file) {

        Item_Details item=createMapper.findProjectByPid(pid);

        String item_img_path=item.getCover_img().substring(0,item.getCover_img().indexOf("cover_img"));

        String url=item_img_path+"content_"+img_id+".avif";

        String path= customSecurityProperties.getStaticPath()+url;

        try{
            file.transferTo(new File(path));
        }catch (IOException e){
            return new ResponseResult<>(CommonCode.FAIL,e.getMessage());
        }

        return new ResponseResult<>(CommonCode.SUCCESS,url);
    }

    @Override  //重新上传作品
    public ResponseResult reUploadProject(Item_Details_Temp temp_item){

        Item_Details item=createMapper.findProjectByPid(temp_item.getPid());

        if(item==null){
            if(temp_item.getIsOK()==1){
                createMapper.updateTempProject(temp_item);
            }else{
                createMapper.createNewProject(temp_item);
            }

        }else{
            temp_item.setIsOK(1);
            createMapper.deleteMyProject(item.getPid(),item.getUid());
            createMapper.createNewProject(temp_item);
        }

        return new ResponseResult(CreateCode.CREATE_SUCCESS,temp_item);
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

        String path= customSecurityProperties.getStaticPath()+"images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

        String newFileName="content_video"+fileName.substring(fileName.indexOf("."));

        return getString(create_id,video,uid,id,path,newFileName);
    }

    @Override
    public Integer getMyProjectCount(String uid,String sortType){
        if(sortType.equals("time")||sortType.equals("hot")){
          return  createMapper.getMyProjectCount(uid);
        }else{
          return  createMapper.getMyTempProjectCount(uid,sortType.equals("waitAgree") ? -1 : 0);
        }
    }

    @Override
    public ResponseResult<List<Item_Details>> getMyProject(String uid, String sortType, int pageNumber) {
        if(sortType.equals("time")){
            return new ResponseResult<>(CommonCode.SUCCESS,createMapper.getMyProjectByTime(uid,pageNumber)) ;
        } else if (sortType.equals("hot")) {
            return new ResponseResult<>(CommonCode.SUCCESS,createMapper.getMyProjectByHot(uid,pageNumber));
        }else if(sortType.equals("takeoff")){
            return new ResponseResult<>(CommonCode.SUCCESS,createMapper.getMyProjectTakeoff(uid,pageNumber));
        }
        return null;
    }

    @Override
    public ResponseResult<List<Item_Details_Temp>> getMyProjectTemp(String uid,String sortType,int pageNumber){
        if(sortType.equals("waitAgree")){
            return new ResponseResult<>(CommonCode.SUCCESS,createMapper.getMyProjectByNoAgree(uid,pageNumber)) ;
        }else if(sortType.equals("draft")){
            return new ResponseResult<>(CommonCode.SUCCESS,createMapper.getMyProjectByDraft(uid,pageNumber));
        }
        return null;
    }

    @Override
    public String updateCoverImg(String pid,MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        String path="";
        String url=createMapper.getMyProjectCoverImgSrc(pid);


        path= customSecurityProperties.getStaticPath()+url.substring(0,url.indexOf("cover_img"));

        String newFileName="cover_img.avif";

        try{
            file.transferTo(new File(path+"\\"+newFileName));
        }catch (Exception e){
            e.printStackTrace();
            return "异常情况";
        }

        return url;
    }

    @Override
    public ResponseResult deleteMyProject(String pid) {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        if(createMapper.findProjectByPid(pid)==null){
            return new ResponseResult(CommonCode.SUCCESS,"作品不存在");
        }else{
            createMapper.deleteMyProject(pid,user.getUid());
            return new ResponseResult(CommonCode.SUCCESS,"删除成功");
        }


    }

    @Override
    public ResponseResult<String> takeoffProject(String pid){
        if(createMapper.takeoffProject(pid)==0){
            return new ResponseResult<>(CommonCode.SUCCESS,"作品不存在");
        }else{
            return new ResponseResult<>(CommonCode.SUCCESS,"下架成功");
        }
    }

    @Override
    public ResponseResult<String> reCoverProjectByPid(String pid) {
        Item_Details item=createMapper.findProjectByPid(pid);
        Item_Details_Temp temp_item=new Item_Details_Temp();
        BeanUtils.copyProperties(item,temp_item);
        if(item==null){
            return new ResponseResult<>(CreateCode.RECOVER_FAILURE,null);
        }else{
            try{
                createMapper.createNewProject(temp_item);
                createMapper.deleteMyProject(pid,item.getUid());
            }catch(Exception e){
                e.printStackTrace();
            }
            return new ResponseResult<>(CreateCode.RECOVER_SUCCESS);
        }

    }
}
