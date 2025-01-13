package com.chen.service.user;

import com.chen.mapper.create.CreateMapper;
import com.chen.mapper.create.CreateTempMapper;
import com.chen.pojo.page.Posts;
import com.chen.pojo.page.Posts_Takeoff;
import com.chen.pojo.page.Posts_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.repository.MongoDataPageAble;
import com.chen.repository.create.PostRepository;
import com.chen.repository.create.PostTakeoffRepository;
import com.chen.repository.create.PostTempRepository;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.CreateCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.CustomSecurityProperties;
import com.chen.utils.util.RedisCache;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CreateServiceImpl implements CreateService{


    private final UserDetailService userDetailService;

    private final PostRepository postRepository;
    private final PostTempRepository postTempRepository;
    private final PostTakeoffRepository postTakeoffRepository;

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
        String path= customSecurityProperties.getStaticPath()+"images/user_data/"+uid+"/project_data/"+create_id+"/"+id;
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

        String path= customSecurityProperties.getStaticPath()+"images/user_data/"+uid+"/project_data/"+create_id+"/"+id;

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
            file.transferTo(new File(path+"/"+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "异常情况";
        }

        return url;
    }

    @Override   //上传新作品实现
    public ResponseResult<List<String>> newProject(Posts_Temp temp_item, String uid) {

        try{
            String id=redisCache.getCacheObject(uid+"create_id:");
            temp_item.setPid(id);

//            if(temp_item.getDetail_type().equals("article") && sensitiveWordBs.contains(temp_item.getContent())){
//                List<String> sensitiveWordList= sensitiveWordBs.findAll(temp_item.getContent());
//                return new ResponseResult<>(CreateCode.CONTAIN_SENSITIVEWORD,sensitiveWordList);
//            }
            postTempRepository.insert(temp_item);

            redisCache.deleteObject(uid+"create_id:");

        }catch(Exception e){
            e.printStackTrace();
            return new ResponseResult<>(CommonCode.FAIL);
        }

        return new ResponseResult<>(CreateCode.CREATE_SUCCESS);

    }

    @Override
    public ResponseResult<Posts_Temp> saveTempProject(Posts_Temp temp_item, String uid) {
        try{
            String id =redisCache.getCacheObject(uid+"create_id:");
            temp_item.setPid(id);
            postTempRepository.save(temp_item);
            return new ResponseResult<>(CommonCode.SUCCESS,temp_item);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult<>(CommonCode.FAIL);
        }
    }

    @Override   //编辑已发布内容图片
    public ResponseResult<String> updateContentImg(String pid, String img_id,MultipartFile file) {

        Posts item=postRepository.findById(pid).orElse(null);
        if(item!=null){
            try{
                String item_img_path=item.getCoverImg().substring(0,item.getCoverImg().indexOf("cover_img"));

                String url=item_img_path+"content_"+img_id+".avif";

                String path= customSecurityProperties.getStaticPath()+url;

                file.transferTo(new File(path));
                return new ResponseResult<>(CommonCode.SUCCESS,url);
            }catch (IOException e){
                return new ResponseResult<>(CommonCode.FAIL,e.getMessage());
            }
        }else{
            return new ResponseResult<>(CommonCode.FAIL);
        }

    }

    @Override  //重新上传作品
    public ResponseResult<Posts_Temp> reUploadProject(Posts_Temp temp_item){

        Posts item=postRepository.findById(temp_item.getPid()).orElse(null);

        if(item==null){
            Posts_Temp temp=postTempRepository.findById(temp_item.getPid()).orElse(null);
            if(temp!=null){
                temp=temp_item;
                postTempRepository.save(temp);
            }else{
                return new ResponseResult<>(CommonCode.FAIL);
            }
        }else{
            temp_item.setIsOK(1);
            postRepository.deleteById(item.getPid());
            postTempRepository.insert(temp_item);
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

        String path= customSecurityProperties.getStaticPath()+"images/user_data/"+uid+"/project_data/"+create_id+"/"+id;

        String newFileName="content_video"+fileName.substring(fileName.indexOf("."));

        return getString(create_id,video,uid,id,path,newFileName);
    }

    @Override
    public ResponseResult<Integer> getMyProjectCount(String uid,String sortType){
        if(sortType.equals("createTime")||sortType.equals("readTimes")){
          return new ResponseResult<>(CommonCode.SUCCESS,postRepository.countByUid(uid));
        }else{
          return new ResponseResult<>(CommonCode.SUCCESS,postTempRepository.countByUid(uid));
        }
    }

    @Override
    public ResponseResult getMyProject(int pageNumber, int pageSize, String sortField, String sortKeywords) {
        Oauth2UserinfoResult loginUser=userDetailService.getLoginUserInfo();
        MongoDataPageAble pageable=new MongoDataPageAble(pageNumber,pageSize, Sort.by(Sort.Direction.DESC,sortField));
        return switch (sortField) {
            case "createTime" -> new ResponseResult(CommonCode.SUCCESS,postRepository.findByUidOrderByCreateTimeDesc(loginUser.getUid(),pageable).stream().toList());
            case "readTimes" ->  new ResponseResult(CommonCode.SUCCESS,postRepository.findByUidOrderByReadTimesDesc(loginUser.getUid(),pageable).stream().toList());
            case "takeoffTime" -> new ResponseResult(CommonCode.SUCCESS,postTakeoffRepository.findByUidOrderByTakeoffTimeDesc(loginUser.getUid(),pageable).stream().toList());
            default -> null;
        };

    }

    @Override
    public ResponseResult<List<Posts_Temp>> getMyProjectTemp(int pageNumber, int pageSize, String sortField, String sortKeywords){
        Oauth2UserinfoResult loginUser=userDetailService.getLoginUserInfo();
        MongoDataPageAble pageable=new MongoDataPageAble(pageNumber,pageSize, Sort.by(Sort.Direction.DESC,sortField));
        if(sortField.equals("waitAgree")){
            return new ResponseResult<>(CommonCode.SUCCESS,postTempRepository.findByIsOKAndUidOrderByReadTimesDesc(1,loginUser.getUid(),pageable).stream().toList()) ;
        }else if(sortField.equals("draft")){
            return new ResponseResult<>(CommonCode.SUCCESS,postTempRepository.findByIsOKAndUidOrderByReadTimesDesc(0,loginUser.getUid(),pageable).stream().toList());
        }
        return null;
    }

    @Override
    public ResponseResult<String> updateCoverImg(String pid,MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        String path="";
        String url="";
        Posts post=postRepository.findById(pid).orElse(null);
        if(post!=null){
            url=post.getCoverImg();
        }else{
            return new ResponseResult<>(CreateCode.POST_UNEXIST);
        }
        path= customSecurityProperties.getStaticPath()+url.substring(0,url.indexOf("cover_img"));

        String newFileName="cover_img.avif";

        try{
            file.transferTo(new File(path+"\\"+newFileName));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }

        return new ResponseResult<>(CommonCode.SUCCESS,url);
    }

    @Override
    public ResponseResult deleteMyProject(String pid,String postState) {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        switch (postState){
            case "normal"-> postRepository.deleteById(pid);
            case "waitAgree", "draft" -> postTempRepository.deleteById(pid);
            case "takeoff" -> postTakeoffRepository.deleteById(pid);
        }

        return new ResponseResult(CreateCode.DELETE_SUCCESS);
    }

    @Override
    public ResponseResult<String> takeoffProject(String pid){

            try {
                Posts item=postRepository.findById(pid).orElse(null);
                if(item!=null){
                    postRepository.deleteById(pid);
                    Posts_Temp temp_item=new Posts_Temp();
                    BeanUtils.copyProperties(item,temp_item);
                    temp_item.setIsOK(-2);
                    postTempRepository.insert(temp_item);
                    return new ResponseResult<>(CreateCode.TAKEOFF_SUCCESS);
                }else{
                    return new ResponseResult<>(CommonCode.FAIL);
                }

            }catch (Exception e){
                return new ResponseResult<>(CommonCode.SUCCESS,"作品不存在");
            }
    }

    @Override
    public ResponseResult reCoverProjectByPid(String pid) {

            try{
                Posts_Takeoff post=postTakeoffRepository.findById(pid).orElse(null);

                if(post!=null){
                    postTakeoffRepository.deleteById(pid);
                    postTempRepository.insert(post);
                }
                return new ResponseResult<>(CreateCode.RECOVER_SUCCESS);
            }catch(Exception e){
                e.printStackTrace();
                return new ResponseResult<>(CreateCode.RECOVER_FAILURE,null);
            }

    }
}
