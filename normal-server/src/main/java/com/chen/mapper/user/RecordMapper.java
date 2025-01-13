package com.chen.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.page.Posts;
import com.chen.pojo.record.Diary_Book;
import com.chen.pojo.user.Oauth2UserinfoResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Mapper
@Repository
public interface RecordMapper extends BaseMapper<Objects> {

    Oauth2UserinfoResult getUserInfo(String uid);

    List<Posts> getUserProject(String uid);

    @Select("select subscription_count from users where uid=#{uid}")
    Long getUserSubscribedCount(String uid);

    List<Diary_Book> getDiaryBookList(String uid);

    int newDiaryBook(String uid);

}
