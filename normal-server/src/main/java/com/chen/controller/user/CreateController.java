package com.chen.controller.user;

import com.chen.pojo.page.Posts_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.service.user.CreateService;
import com.chen.service.user.UserDetailService;

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
    public ResponseResult newProjectImg(@PathVariable(required = false) String create_id,@PathVariable(required = false) String pid,@PathVariable String img_id,@RequestParam("file") MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        if(pid==null){
            return createService.newProjectImg(create_id,img_id,file,user.getUid());
        }else{
            return createService.updateContentImg(pid,img_id,file);
        }

    }

    @PostMapping("/newProject")  //提交新作品接口
    public ResponseResult newProject(@RequestBody Posts_Temp temp_item){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        return createService.newProject(temp_item,user.getUid());
    }


    @PostMapping("/updateCoverImg/{pid}")  //更新封面
    public ResponseResult updateCoverImg(@PathVariable String pid,@RequestParam("file") MultipartFile file){

        return createService.updateCoverImg(pid,file);
    }

    @PostMapping("/reUploadProject")  //更新作品
    public ResponseResult reUploadProject(@RequestBody Posts_Temp temp_item){

        return createService.reUploadProject(temp_item);
    }

    @PostMapping("/uploadVideo/{create_id}")   //上传视频
    public ResponseResult uploadVideo(@PathVariable String create_id,@RequestParam("file") MultipartFile video){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String url=createService.uploadVideo(create_id,video,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,url);
    }

    @PostMapping("/saveTempProject")   //保存草稿
    public ResponseResult saveTempProject(@RequestBody Posts_Temp temp_item){
        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        return createService.saveTempProject(temp_item,user.getUid());
    }

    @GetMapping("/getMyProjectCount/{uid}/{sortType}")   //获取我的作品数量
    public ResponseResult<Integer> getMyProjectCount(@PathVariable String uid,@PathVariable String sortType){
        return createService.getMyProjectCount(uid,sortType);
    }

    @PostMapping("/getMyProject/{pageNumber}/{pageSize}/{sortField}/{sortKeywords}")  //获取作品接口
    public ResponseResult getMyProject(@PathVariable Integer pageNumber ,@PathVariable Integer pageSize,@PathVariable String sortField,@PathVariable String sortKeywords){

        if(sortField.equals("waitAgree") || sortField.equals("draft")){
            return createService.getMyProjectTemp(pageNumber,pageSize,sortField,sortKeywords);
        }

        return createService.getMyProject(pageNumber,pageSize,sortField,sortKeywords);
    }

    @PostMapping("/deleteMyProject")  //删除作品
    public ResponseResult<String> deleteMyProject(@RequestBody Map<String,Object> params){
        String pid=params.get("pid").toString();
        String postState=params.get("postState").toString();
        return createService.deleteMyProject(pid,postState);
    }

    @PostMapping("/takeoffProject")  //下架作品
    public ResponseResult<String> takeoffProject(@RequestBody Map<String,Object> params){
        String pid=params.get("pid").toString();
        return createService.takeoffProject(pid);
    }

    @PostMapping("/recoverProject")  //恢复作品
    public ResponseResult<String> reCoverProject(@RequestBody Map<String,Object> params){
        String pid=params.get("pid").toString();
        return createService.reCoverProjectByPid(pid);
    }
}
