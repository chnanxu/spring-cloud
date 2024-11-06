package com.chen.controller;


import com.chen.pojo.community.Community;
import com.chen.pojo.community.CommunityLeftNav;
import com.chen.pojo.community.CommunityModule;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.user.OnlineUser;
import com.chen.service.CommunityService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    private static Queue<OnlineUser> userQueue=new LinkedList<>();

    //社区相关接口--------------------------------------------------------------------------------------------------------


    @GetMapping("/getCommunityList/{queryType}/{pageNum}")
    public ResponseResult getCommunityList(@PathVariable Integer queryType, @PathVariable int pageNum){

        return communityService.getCommunityListByQueryType(queryType,pageNum);
    }

    @GetMapping("/getTotalHotCommunity")  //获取热门社区
    public ResponseResult<List<Map>> getTotalHotCommunity(){

        List<Map> result=communityService.getTotalHotCommunity();

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping("/getCommunityDetails/{id}/{sortType}/{pageNum}")    //社区详情
    public ResponseResult<List<Item_Details>> getCommunityDetails(@PathVariable long id,@PathVariable String sortType,@PathVariable int pageNum){

        List<Item_Details> result=communityService.getCommunityDetailsBySortType(id,pageNum,sortType);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping("/getCommunityLeftNav/{community_id}")
    public ResponseResult<List<CommunityLeftNav>> getCommunityLeftNav(@PathVariable long community_id){

        List<CommunityLeftNav> result= communityService.getCommunityLeftNav(community_id);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping("/getCommunityModule/{community_id}")
    public ResponseResult<List<CommunityModule>> getCommunityModule(@PathVariable long community_id){

        return communityService.getCommunityModule(community_id);
    }

    @GetMapping("/getNews/{community_id}")
    public ResponseResult<List<Item_Details>> getExclusiveData(@PathVariable long community_id){

        return communityService.getExclusiveData(community_id);
    }

    @GetMapping("/getStrategy/{community_id}")
    public ResponseResult<List<Item_Details>> getStrategy(@PathVariable long community_id){
        return communityService.getStrategy(community_id);
    }

    @PostMapping({"/joinUser","/pushUser"})
    public ResponseResult onlineUserList(@RequestBody(required = false) OnlineUser user){

        userQueue.offer(user);

        return new ResponseResult(CommonCode.SUCCESS,userQueue);

    }

}
