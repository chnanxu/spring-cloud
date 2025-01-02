package com.chen.controller.user;

import com.chen.pojo.read.Book_Detail;
import com.chen.service.user.ReadService;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/read")
@RequiredArgsConstructor
public class ReadController {

    private final ReadService readService;

    @GetMapping("/{bid}")
    public ResponseResult<Map<String,List<String>>> getBookDetail(@PathVariable Integer bid){
        return readService.getBookDetail(bid);
    }

    @GetMapping("/getBookChapterList/{bid}")
    public ResponseResult getBookChapterList(@PathVariable Integer bid){
        return readService.getBookChapterList(bid);
    }

    @GetMapping("/{bid}/chapterPage")
    public ResponseResult<Map<Integer,String>> getChapterPage(@PathVariable Integer bid,@RequestParam("chapter_name") String chapter_name){
        return readService.getChapterPage(bid,chapter_name);
    }

    @GetMapping("/{bid}/updateReadRecord")
    public ResponseResult<String> updateReadRecord(@PathVariable Integer bid,
                                                   @RequestParam("chapter_name") String chapter_name,
                                                   @RequestParam("start_page") Integer start_page,
                                                   @RequestParam("end_page") Integer end_page){
        return readService.updateReadRecord(bid,chapter_name,start_page,end_page);
    }

    @PostMapping("/uploadBook")
    public ResponseResult<String> uploadBook(@RequestParam("file") MultipartFile file,@RequestParam("img") MultipartFile img){
        return readService.uploadBook(file,img);
    }

    @GetMapping("/getBookList")
    public ResponseResult<List<Book_Detail>> getBookList(){
        return readService.getBookList();
    }
}
