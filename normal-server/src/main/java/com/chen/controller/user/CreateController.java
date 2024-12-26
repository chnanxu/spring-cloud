package com.chen.controller.user;

import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.service.CreateService;
import com.chen.service.UserDetailService;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/create")
public class CreateController {

    private final CreateService createService;

    private final UserDetailService userDetailService;

    /**
     * 创作中心
     * @param create_id
     * @param file
     * @return
     */
    @PostMapping("/newCoverImg/{create_id}") //封面上传接口
    public ResponseResult newCoverImg(@PathVariable String create_id, @RequestParam("file") MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String message= createService.newCoverImg(create_id,file,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,message);
    }


    @PostMapping(value={"/newProjectImg/{create_id}/{img_id}","/updateProjectImg/{pid}/{img_id}"})  //内容图片上传接口
    public ResponseResult newProjectImg(@PathVariable(required = false) String create_id,@PathVariable(required = false) Long pid,@PathVariable String img_id,@RequestParam("file") MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        if(pid==null){
            return createService.newProjectImg(create_id,img_id,file,user.getUid());
        }else{
            return createService.updateContentImg(pid,img_id,file);
        }

    }

    @PostMapping("/newProject")  //提交新作品接口
    public ResponseResult newProject(@RequestBody Item_Details_Temp temp_item){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        return createService.newProject(temp_item,user.getUid());
    }


    @PostMapping("/updateCoverImg/{pid}")  //更新封面
    public ResponseResult updateCoverImg(@PathVariable Long pid,@RequestParam("file") MultipartFile file){

        String url=createService.updateCoverImg(pid,file);

        return new ResponseResult(CommonCode.SUCCESS,url);
    }

    @PostMapping("/reUploadProject")  //更新作品
    public ResponseResult reUploadProject(@RequestBody Item_Details_Temp temp_item){

        return createService.reUploadProject(temp_item);
    }

    @PostMapping("/uploadVideo/{create_id}")   //上传视频
    public ResponseResult uploadVideo(@PathVariable String create_id,@RequestParam("file") MultipartFile video){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String url=createService.uploadVideo(create_id,video,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,url);
    }

    @PostMapping("/saveTempProject")   //保存草稿
    public ResponseResult saveTempProject(@RequestBody Item_Details_Temp temp_item){
        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        return createService.newProject(temp_item,user.getUid());
    }

    @GetMapping("/getMyProjectCount/{uid}/{sortType}")   //获取我的作品数量
    public ResponseResult<Integer> getMyProjectCount(@PathVariable String uid,@PathVariable String sortType){
        return new ResponseResult(CommonCode.SUCCESS,createService.getMyProjectCount(uid,sortType));
    }

    @PostMapping("/getMyProject/{uid}/{sortType}/{pageNumber}")  //获取作品接口
    public ResponseResult getMyProject(@PathVariable String uid,@PathVariable String sortType,@PathVariable int pageNumber){

        if(sortType.equals("waitAgree") || sortType.equals("draft")){
            return createService.getMyProjectTemp(uid,sortType,pageNumber*16-16);
        }

        return createService.getMyProject(uid,sortType,pageNumber*16-16);
    }

    @PostMapping("/deleteMyProject")  //删除作品
    public ResponseResult<String> deleteMyProject(@RequestBody Map<String,Object> params){
        long pid=Long.parseLong(params.get("pid").toString());
        return createService.deleteMyProject(pid);
    }

    @PostMapping("/takeoffProject")  //下架作品
    public ResponseResult<String> takeoffProject(@RequestBody Map<String,Object> params){
        long pid=Long.parseLong(params.get("pid").toString());
        return createService.takeoffProject(pid);
    }

    @PostMapping("/recoverProject")  //恢复作品
    public ResponseResult<String> reCoverProject(@RequestBody Map<String,Object> params){
        long pid=Long.parseLong(params.get("pid").toString());
        return createService.reCoverProjectByPid(pid);
    }
}
