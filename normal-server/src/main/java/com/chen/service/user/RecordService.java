package com.chen.service.user;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {
    Oauth2UserinfoResult getUserInfo(String keywords);

    List<Item_Details> getUserProject(String keywords);

    ResponseResult<List<Diary_Book>> getDiaryBookList(String uid);

    ResponseResult<String> newDiaryBookList(String uid);
}
