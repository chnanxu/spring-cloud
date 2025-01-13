package com.chen.service.user;

import com.chen.pojo.page.Posts;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {
    ResponseResult<Oauth2UserinfoResult> getUserInfo(String keywords);

    ResponseResult<List<Posts>> getUserProject(String keywords,Integer pageNumber,Integer pageSize);

    ResponseResult<Long> getSubscribeCount(String uid);

    ResponseResult<List<Diary_Book>> getDiaryBookList(String uid);

    ResponseResult<String> newDiaryBookList(String uid);
}
