package com.chen.service;

import com.chen.mapper.ReadMapper;
import com.chen.pojo.read.Book_Detail;
import com.chen.pojo.read.User_Read_Record;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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

    @Override  //获取图书详情
    public ResponseResult<Map<String,List<String>>> getBookDetail(Integer bid) {

        Book_Detail book=readMapper.getBookDetail(bid);
        String path=customSecurityProperties.getStaticPath()+book.getSave_path();
        Map<String,List<String>> result=new HashMap<>();
        try{
            ProcessBuilder pb=new ProcessBuilder("python","normal-server/src/main/resources/python/textFilter.py",path);
            pb.environment().put("PYTHONIOENCODING","utf-8");
            Process process=pb.start();

            try (BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"))){
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
    public ResponseResult<String> uploadBook(MultipartFile file) {

        if(file.isEmpty()){
            return new ResponseResult<>(CommonCode.FAIL,"文件不能为空");
        }else{
            Oauth2UserinfoResult user=userDetailService.getLoginUserInfo();
            String path = customSecurityProperties.getStaticPath()+"\\images\\user_data\\"+user.getUid()+"\\book";

            File f=new File(path);
            if(!f.exists()){
                f.mkdir();
            }


            try{
                String fileName=file.getOriginalFilename();
                String fileType=fileName.substring(fileName.lastIndexOf("."));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
                String newFileName=sdf.format(System.currentTimeMillis())+fileType;
                file.transferTo(new File(path+"\\"+newFileName));

                String url = "images/user_data/"+user.getUid()+"/book/"+newFileName;

                Book_Detail book=new Book_Detail();
                book.setTitle(fileName.substring(0,fileName.lastIndexOf(".")));
                book.setAuthor(user.getUname());
                book.setUid(user.getUid());
                book.setSave_path(url);

                readMapper.insertBook(book);

            }catch(IOException ioe){
                ioe.printStackTrace();
                return new ResponseResult<>(CommonCode.FAIL,"");
            }

        }

        return new ResponseResult<>(CommonCode.SUCCESS,"上传成功");
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
