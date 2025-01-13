package com.chen.controller;


import com.chen.pojo.page.All_Type;
import com.chen.pojo.page.HotTag;
import com.chen.pojo.page.Posts;
import com.chen.service.IndexService;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;


    @GetMapping("/getHeaderItem")  //导航栏
    public ResponseResult<List<String>> getHeaderItem(){

        List<String> result=indexService.getHeaderItem();
        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping("/getTypeList")  //类型列表
    public ResponseResult<List<All_Type>> getTypeList(){

        List<All_Type> result=indexService.getTypeList();
        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping(value = {"/getHotTag/{keywords}/{pageNumber}","/getHotTag/{pageNumber}"})
    public ResponseResult<List<HotTag>> getHotTag(@PathVariable(required = false) String keywords,@PathVariable int pageNumber){
        return indexService.getHotTag(keywords,pageNumber);
    }


    @GetMapping("/getLeftNavbar") //左侧边栏
    public ResponseResult<List<String>> getLeftNavbar(){

        List<String> result=indexService.getLeftNavbar();

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

    @GetMapping(value={"/","/index/{typeId}/{pageNumber}/{pageSize}/{articleType}"})
    public ResponseResult<List<Posts>> load(@PathVariable int typeId,@PathVariable int pageNumber,@PathVariable int pageSize, @PathVariable String articleType){

        return indexService.getIndex(typeId,pageNumber,pageSize,articleType);

    }

    @GetMapping(value={"/getAnnouncement/{announcementCommunitySortType}/{announcementSortType}"})
    public ResponseResult<Map<Integer, List<Posts>>> getAnnouncement(@PathVariable String announcementCommunitySortType, @PathVariable String announcementSortType){

        return indexService.getAnnouncement(announcementCommunitySortType,announcementSortType);

    }

    @GetMapping("/getAnnouncementByCommunityId/{community_id}/{announcementSortType}")
    public ResponseResult<Map<String,List<Posts>>> getAnnouncementByCommunityName(@PathVariable Integer community_id, @PathVariable String announcementSortType){

        return indexService.getAnnouncementByCommunityId(community_id,announcementSortType);
    }

    @GetMapping("/user/item")  //用户主页列表项接口
    public ResponseResult<String> userItem(){
        List<String> userItem=indexService.findUserItem();

        return new ResponseResult(CommonCode.SUCCESS,userItem);
    }

    @GetMapping("/create/leftItem")  //
    public ResponseResult<List<String>> createLeftItem(){

        List<String> leftItem=indexService.finCreateLeftItem();

        return new ResponseResult<>(CommonCode.SUCCESS,leftItem);
    }

    @GetMapping(value = {"/getSearchTempList/{text}","getSearchTempList/"})
    public ResponseResult<List<String>> getSearchTempList(@PathVariable(required = false) String text){
        List<String> result=new ArrayList<>();
        if(text==null){

        }else{
            result=indexService.searchTempList(text);
        }

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }


    @GetMapping(value={"/getSearchDetails/{keywords}/{articleType}/{pageNum}","/getSearchDetails/{articleType}/{pageNum}"})
    public ResponseResult<List<Posts>> getSearchDetails(@PathVariable(required = false) String keywords, @PathVariable String articleType, @PathVariable int pageNum){

        List<Posts>  result=indexService.getSearchDetails(keywords,articleType,pageNum*24-24);

        return new ResponseResult<>(CommonCode.SUCCESS,result);
    }

}
