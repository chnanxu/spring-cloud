package com.chen.controller;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.Item_Details_Temp;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.service.CreateService;
import com.chen.service.UserDetailService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/create")
public class CreateController {

    private final CreateService createService;

    private final UserDetailService userDetailService;

    @GetMapping("/getCommunityList/{queryType}/{pageNum}")
    public ResponseResult getCommunityList(@PathVariable String queryType,@PathVariable int pageNum){

        return new ResponseResult(CommonCode.SUCCESS,createService.getCommunityListByQueryType(queryType,pageNum));
    }

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


    @PostMapping("/newProjectImg/{create_id}/{img_id}")  //内容图片上传接口
    public ResponseResult newProjectImg(@PathVariable String create_id,@PathVariable String img_id,@RequestParam("file") MultipartFile file){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String message=createService.newProjectImg(create_id,img_id,file,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,message);
    }


    @PostMapping("/newProject")  //提交新作品接口
    public ResponseResult newProject( @RequestBody Item_Details_Temp temp_item){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        String message=createService.newProject(temp_item,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,message);
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

        String message=createService.newProject(temp_item,user.getUid());

        return new ResponseResult(CommonCode.SUCCESS,message);
    }

    @GetMapping("/getMyProjectCount/{uid}/{sortType}")   //获取我的作品数量
    public ResponseResult getMyProjectCount(@PathVariable String uid,@PathVariable String sortType){
        return new ResponseResult(CommonCode.SUCCESS,createService.getMyProjectCount(uid,sortType));
    }

    @PostMapping("/getMyProject/{uid}/{sortType}/{pageNumber}")  //获取作品接口
    public ResponseResult getMyProject(@PathVariable String uid,@PathVariable String sortType,@PathVariable int pageNumber){

        if(sortType.equals("waitAgree") || sortType.equals("draft")){
            return new ResponseResult(CommonCode.SUCCESS,createService.getMyProjectTemp(uid,sortType,pageNumber*6-6));
        }
        List<Item_Details> result= createService.getMyProject(uid,sortType,pageNumber*6-6);

        return new ResponseResult(CommonCode.SUCCESS,result);
    }

    @PostMapping("/updateCoverImg/{pid}")
    public ResponseResult updateCoverImg(@PathVariable long pid,@RequestParam("file") MultipartFile file){

        String url=createService.updateCoverImg(pid,file);

        return new ResponseResult(CommonCode.SUCCESS,url);
    }


}
