package com.chen.service.user;

import com.chen.mapper.user.RecordMapper;
import com.chen.pojo.page.Posts;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.repository.MongoDataPageAble;
import com.chen.repository.create.PostRepository;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.RecordCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;

    private final UserDetailService userDetailService;
    private final PostRepository postRepository;

    @Override
    public ResponseResult<Oauth2UserinfoResult> getUserInfo(String keywords) {
        if(keywords==null){
            return new ResponseResult<>(CommonCode.FAIL);
        }else{
            return new ResponseResult<>(CommonCode.SUCCESS,recordMapper.getUserInfo(keywords));
        }

    }

    @Override
    public ResponseResult<List<Posts>> getUserProject(String keywords,Integer pageNumber,Integer pageSize) {
        Oauth2UserinfoResult user= userDetailService.getLoginUserInfo();
        MongoDataPageAble pageable=new MongoDataPageAble(pageNumber,pageSize, Sort.by(Sort.Direction.DESC,"createTime"));
        List<Posts> posts;
        if(keywords==null){
            if(user.getUid()==null){
                return new ResponseResult<>(CommonCode.FAIL);
            }else{
                posts=postRepository.findByUidOrderByCreateTimeDesc(user.getUid(),pageable).stream().toList();
                return new ResponseResult<>(CommonCode.SUCCESS,posts);
            }
        }else{
            posts=postRepository.findByUidOrderByCreateTimeDesc(keywords,pageable).stream().toList();
            return new ResponseResult<>(CommonCode.SUCCESS, posts);
        }

    }

    @Override
    public ResponseResult<Long> getSubscribeCount(String uid) {
        return new ResponseResult<>(CommonCode.SUCCESS,recordMapper.getUserSubscribedCount(uid));
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
