package com.chen.mapper.user;

import com.chen.pojo.read.Book_Detail;
import com.chen.pojo.read.User_Read_Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReadMapper {
    void insertBook(Book_Detail book);

    List<Book_Detail> getBookList(String uid);

    Book_Detail getBookDetail(Integer bid);

    User_Read_Record getUserReadRecord(String uid,Integer bid);

    void updateReadRecord(String uid,Integer bid,String chapter_name,Integer start_page,Integer end_page);

    void insertReadRecord(String uid,Integer bid,String book_title,String chapter_name,Integer start_page,Integer end_page);
}
