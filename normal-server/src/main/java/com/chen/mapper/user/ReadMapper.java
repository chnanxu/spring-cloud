package com.chen.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.pojo.read.Book_Detail;
import com.chen.pojo.read.User_Read_Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReadMapper extends BaseMapper<Book_Detail> {

    @Insert("insert into book_detail values(default,#{title},#{author},#{uid},CURTIME(),#{save_path},#{cover_img},#{file_type})")
    void insertBook(Book_Detail book);

    List<Book_Detail> getBookList(String uid);

    User_Read_Record getUserReadRecord(String uid,Integer bid);

    void updateReadRecord(String uid,Integer bid,String chapter_name,Integer start_page,Integer end_page);

    void insertReadRecord(String uid,Integer bid,String book_title,String chapter_name,Integer start_page,Integer end_page);
}
