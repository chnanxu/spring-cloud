package com.chen.mapper;


import com.chen.pojo.page.Item_Comments;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.page.My_Earnings;
import com.chen.pojo.user.UserLikeComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PageMapper {

    void addReadTimes(long pid);

    Item_Details getPageDetails(long pid);

    List<Item_Details> getAuthorOtherByUid(String uid,long pid);

    List<Item_Comments> getPageDetailsComments(long pid,int pageNumber);

    List<Item_Comments> getSonComments(long comment_id);

    UserLikeComment getUserLikeComments(String uid,long pid,long comment_id);

    @Options(useGeneratedKeys = true,keyColumn = "comment_id")
    long submitComment(Item_Comments commentData);

    void addCommentLikeTimes(long comment_id);

    void substractCommentLikeTimes(long comment_id);

    void subtractDetailLikeTimes(long pid);

    void addDetailLikeTimes(long pid);

    String getReCommentUname(long to_commentID);

    void deleteComment(long commentId);

    void updateItemCommentSize(long pid);

    List<Item_Comments> getAllSonComment(long comment_id);

    int supportAuthor(My_Earnings item);

}
