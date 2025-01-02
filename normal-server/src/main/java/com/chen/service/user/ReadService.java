package com.chen.service.user;

import com.chen.pojo.read.Book_Detail;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ReadService {

    ResponseResult<Map<String,List<String>>> getBookDetail(Integer bid);

    ResponseResult getBookChapterList(Integer bid);

    ResponseResult<Map<Integer,String>> getChapterPage(Integer bid,String chapterName);

    ResponseResult<String> updateReadRecord(Integer bid,String chapter_name,Integer start_page,Integer end_page);

    ResponseResult<String> uploadBook(MultipartFile file,MultipartFile img);

    ResponseResult<List<Book_Detail>> getBookList();

}
