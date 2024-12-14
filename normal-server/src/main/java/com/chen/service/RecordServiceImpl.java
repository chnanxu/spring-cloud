package com.chen.service;

import com.chen.mapper.RecordMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.RecordCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;

    private final UserDetailService userDetailService;

    @Override
    public Oauth2UserinfoResult getUserInfo(String keywords) {
        if(keywords==null){

        }else{
            return recordMapper.getUserInfo(keywords);
        }
        return null;
    }

    @Override
    public List<Item_Details> getUserProject(String keywords) {

        if(keywords==null){
           Oauth2UserinfoResult user= userDetailService.getLoginUserInfo();
            if(user.getUid()==null){

            }else{
                return recordMapper.getUserProject(user.getUid());
            }
        }else{
            return recordMapper.getUserProject(keywords);
        }

        return null;
    }

    @Override
    public ResponseResult<List<Diary_Book>> getDiaryBookList(String uid) {

        List<Diary_Book> result=recordMapper.getDiaryBookList(uid);

        return new ResponseResult<>(CommonCode.SUCCESS,result);

    }

    @Override
    public ResponseResult<String> newDiaryBookList(String uid) {

        int result=0;
        result=recordMapper.newDiaryBook(uid);

        if(result==0){
            return new ResponseResult<>(RecordCode.NEW_DIARYBOOK_FAILURE);
        }else{
            return new ResponseResult<>(RecordCode.NEW_DIARYBOOK_SUCCESS);
        }

    }
}
