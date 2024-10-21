package com.chen.service;

import com.chen.mapper.CreateMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;

import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import result.CommonCode;
import result.CreateCode;
import result.ResponseResult;
import result.UserCode;

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

    private static final String STATIC_PATH="D:\\developTools\\Workspace\\static";

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


        String path=STATIC_PATH+"\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;
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


        String path=STATIC_PATH+"\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

        String newFileName="content_"+img_id+".avif";

        String resultUrl=getString(create_id, file, uid, id, path, newFileName);

        return new ResponseResult<>(CommonCode.SUCCESS,resultUrl);
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
    public ResponseResult<List<String>> newProject(Item_Details_Temp temp_item, String uid) {

        try{
            String id=redisCache.getCacheObject(uid+"create_id:");
            temp_item.setPid(id);

//            SensitiveWordBs sensitiveWord= SensitiveWordBs.newInstance()
//                    .wordResultCondition(WordResultConditions.englishWordMatch())
//                    .init();
//
//
//            if(temp_item.getDetail_type().equals("article") && sensitiveWord.contains(temp_item.getContent())){
//                List<String> sensitiveWordList=SensitiveWordHelper.findAll(temp_item.getContent());
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

    @Override
    public ResponseResult<String> updateContentImg(String pid, String img_id,MultipartFile file) {

        Item_Details item=createMapper.findProjectByPid(pid);

        String item_img_path=item.getCover_img().substring(0,item.getCover_img().indexOf("cover_img"));

        String url=item_img_path+"content_"+img_id+".avif";

        String path=STATIC_PATH+"\\"+url;

        try{
            file.transferTo(new File(path));
        }catch (IOException e){
            return new ResponseResult<>(CommonCode.FAIL,e.getMessage());
        }

        return new ResponseResult<>(CommonCode.SUCCESS,url);
    }

    @Override
    public ResponseResult reUploadProject(Item_Details_Temp temp_item){

        Item_Details item=createMapper.findProjectByPid(temp_item.getPid());

        if(item==null){
            createMapper.createNewProject(temp_item);
        }else{
            item.setIsOK(2);
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

        String path=STATIC_PATH+"\\images\\user_data\\"+uid+"\\project_data\\"+create_id+"\\"+id;

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
    public ResponseResult<List<Item_Details>> getMyProject(String uid,String sortType,int pageNumber) {
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


        path=STATIC_PATH+"\\"+url.substring(0,url.indexOf("cover_img"));

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
}
