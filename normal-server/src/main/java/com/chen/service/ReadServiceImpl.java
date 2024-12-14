package com.chen.service;

import com.chen.mapper.ReadMapper;
import com.chen.pojo.read.Book_Detail;
import com.chen.pojo.read.User_Read_Record;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ReadCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReadServiceImpl implements ReadService{

    private final ReadMapper readMapper;
    private final UserDetailService userDetailService;
    private final CustomSecurityProperties customSecurityProperties;

    private static Book_Detail saveBookToPath(String path,String folderName,String fileType,String imgType,MultipartFile file,MultipartFile img,Oauth2UserinfoResult user){

        try{
            if(fileType.toLowerCase().contains("txt")){
                Book_Detail book=new Book_Detail();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
                String newFileName=sdf.format(System.currentTimeMillis())+fileType;
                String imgFileName="cover_img"+imgType;
                //图书文件
                file.transferTo(new File(path+"\\"+newFileName));
                //封面
                img.transferTo(new File(path+"\\"+imgFileName));
                String url = "images/user_data/"+user.getUid()+"/book/"+folderName+"/";
                book.setTitle(folderName);
                book.setAuthor(user.getUname());
                book.setUid(user.getUid());
                book.setSave_path(url+newFileName);
                book.setCover_img(url+imgFileName);
                return book;
            } else if (fileType.toLowerCase().contains("epub")) {
                EpubReader epubReader=new EpubReader();
                Book epubBook=epubReader.readEpub(file.getInputStream());
                List<String> titles=epubBook.getMetadata().getTitles();
                System.out.println(titles);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
            return null;
        }

        return null;
    }

    @Override  //上传图书
    public ResponseResult<String> uploadBook(MultipartFile file,MultipartFile img) {

        if(file.isEmpty()){
            return new ResponseResult<>(CommonCode.FAIL,"文件不能为空");
        }else{
            Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
            //图书
            String fileName=file.getOriginalFilename();
            String folderName=fileName.substring(0,fileName.lastIndexOf('.'));
            String fileType=fileName.substring(fileName.lastIndexOf('.'));
            //封面
            String imgName= img.getOriginalFilename();
            String imgType=imgName.substring(imgName.lastIndexOf('.'));
            String path = customSecurityProperties.getStaticPath()+"\\images\\user_data\\"+user.getUid()+"\\book\\"+folderName;

            File f=new File(path);
            if(!f.exists()){
                f.mkdir();
            }
            Book_Detail book=saveBookToPath(path,folderName,fileType,imgType,file,img,user);
            if(book!=null){
                readMapper.insertBook(book);
            }else{
                return new ResponseResult<>(CommonCode.FAIL,"上传失败");
            }

        }

        return new ResponseResult<>(CommonCode.SUCCESS,"上传成功");
    }

    @Override  //获取图书详情
    public ResponseResult<Map<String,List<String>>> getBookDetail(Integer bid) {

        Book_Detail book=readMapper.getBookDetail(bid);
        if(book==null){
            return new ResponseResult<>(ReadCode.BOOK_NULL);
        }

        String path=customSecurityProperties.getStaticPath()+book.getSave_path();
        Map<String,List<String>> result=new HashMap<>();
        try{
            ProcessBuilder pb=new ProcessBuilder("python","normal-server/src/main/resources/python/textFilter.py",path);
            pb.environment().put("PYTHONIOENCODING","utf-8");
            Process process=pb.start();

            try (BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))){
                String line;
                List<String> chapterList=new ArrayList<>();
                while ((line=reader.readLine())!=null){
                    chapterList.add(line);
                }
                result.put("chapterList",chapterList);
            }
            process.waitFor();

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        User_Read_Record readRecord=readMapper.getUserReadRecord(user.getUid(),bid);
        if(readRecord!=null){
            List<String> recordList=new ArrayList<>();
            recordList.add(readRecord.getChapter_name());
            recordList.add(readRecord.getStart_page().toString());
            result.put("user_read_record",recordList);
        }

        return new ResponseResult<>(CommonCode.SUCCESS,result);

    }

    @Override  //获取章节
    public ResponseResult<Map<Integer,String>> getChapterPage(Integer bid,String chapterName){

        Book_Detail book=readMapper.getBookDetail(bid);
        String path=customSecurityProperties.getStaticPath()+book.getSave_path();
        Map<Integer,String> chapterPage=new HashMap<>();
        try{
            ProcessBuilder pb=new ProcessBuilder("python","normal-server/src/main/resources/python/chapterPage.py",path,chapterName);
            pb.environment().put("PYTHONIOENCODING","utf-8");
            Process process=pb.start();
            try(BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"))){
                String line;
                Integer num=0;
                while ((line=reader.readLine())!=null){
                    chapterPage.put(num,line.trim());
                    num+=1;
                }
            }
            process.waitFor();

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return new ResponseResult<>(CommonCode.SUCCESS,chapterPage);
    }

    @Override
    public ResponseResult<String> updateReadRecord(Integer bid,String chapter_name,Integer start_page,Integer end_page){

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
        if(user.getUid()!=null){
            if(readMapper.getUserReadRecord(user.getUid(),bid)!=null){
                readMapper.updateReadRecord(
                        user.getUid(),
                        bid,
                        chapter_name,
                        start_page,
                        end_page
                );
            }else{
                Book_Detail book=readMapper.getBookDetail(bid);
                readMapper.insertReadRecord(
                        user.getUid(),
                        bid,book.getTitle(),
                        chapter_name,
                        start_page,
                        end_page
                );
            }
        }
        return new ResponseResult<>(CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult<List<Book_Detail>> getBookList() {

        Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();

        if(user.getUid()!=null){
            return new ResponseResult<>(CommonCode.SUCCESS,readMapper.getBookList(user.getUid()));
        }else{
            return new ResponseResult<>(UserCode.NOLOGIN);
        }

    }

}
