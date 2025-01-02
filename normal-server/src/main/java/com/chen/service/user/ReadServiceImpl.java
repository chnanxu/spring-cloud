package com.chen.service.user;

import com.chen.mapper.user.ReadMapper;
import com.chen.pojo.read.Book_Detail;
import com.chen.pojo.read.User_Read_Record;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ReadCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.CustomSecurityProperties;
import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReadServiceImpl implements ReadService{

    private final ReadMapper readMapper;
    private final UserDetailService userDetailService;
    private final CustomSecurityProperties customSecurityProperties;

    private static Book_Detail saveBookToPath(
            String path,   //实际保存路径
            String url,    //数据库存储路径
            String folderName,   //保存文件夹名称
            String fileType,    //文件类型
            String imgType,    //图片类型
            MultipartFile file,  //图书文件
            MultipartFile img,  //自定义封面文件
            Oauth2UserinfoResult user   //登陆用户
    ){

        try{
            if(fileType.toLowerCase().contains("txt")){
                Book_Detail book=new Book_Detail();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
                String newFileName=sdf.format(System.currentTimeMillis())+fileType;
                String imgFileName="cover_img"+imgType;
                //图书文件
                file.transferTo(new File(path+"/"+newFileName));
                //封面
                img.transferTo(new File(path+"/"+imgFileName));

                book.setTitle(folderName);
                book.setAuthor(user.getUname());
                book.setUid(user.getUid());
                book.setSave_path(url+"/"+newFileName);
                book.setCover_img(url+"/"+imgFileName);
                book.setFile_type("txt");
                return book;
            } else if (fileType.toLowerCase().contains("epub")) {
                //图书实体
                Book_Detail book=new Book_Detail();

                EpubReader epubReader=new EpubReader();
                Book epubBook=epubReader.readEpub(file.getInputStream());
                List<Author> authors=epubBook.getMetadata().getAuthors();
                StringBuilder author_result= new StringBuilder();
                for(Author author:authors){
                    author_result.append(author);
                }
                String cover_imgName="cover_img"+imgType;

                file.transferTo(new File(path+"/"+file.getOriginalFilename()));
                img.transferTo(new File(path+"/"+cover_imgName));

                book.setTitle(epubBook.getTitle());
                book.setAuthor(author_result.toString());
                book.setUid(user.getUid());
                book.setSave_path(url+"/"+file.getOriginalFilename());
                book.setCover_img(url+"/"+cover_imgName);
                book.setFile_type("epub");
                return book;
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
            String url="images/user_data/"+user.getUid()+"/book/"+folderName;
            String path = customSecurityProperties.getStaticPath()+url;
            File f=new File(path);
            if(!f.exists()){
                f.mkdir();
            }
            Book_Detail book=saveBookToPath(path,url,folderName,fileType,imgType,file,img,user);
            if(book!=null){
                readMapper.insertBook(book);
            }else{
                return new ResponseResult<>(CommonCode.FAIL,"上传失败");
            }

        }

        return new ResponseResult<>(CommonCode.SUCCESS,"上传成功");
    }

    @Override  //获取图书详情
    public ResponseResult getBookDetail(Integer bid) {

        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override  //获取目录
    public ResponseResult getBookChapterList(Integer bid) {
        Book_Detail book=readMapper.selectById(bid);
        if(book==null){
            return new ResponseResult<>(ReadCode.BOOK_NULL);
        }

        String path=customSecurityProperties.getStaticPath()+book.getSave_path();
        if(book.getFile_type().equals("epub")){
            try{
                EpubReader epubReader=new EpubReader();
                Book epubBook=epubReader.readEpub(new FileInputStream(path));

                //读取章节目录
                TableOfContents tocContents= epubBook.getTableOfContents();
                List<TOCReference> tocReferences= tocContents.getTocReferences();

                Map<String,Map<String,String>> chapterMap=new LinkedHashMap<>();

                for (TOCReference tocReference : tocReferences) {

                    Map<String,String> son_chapterMap=new LinkedHashMap<>();
                    if(!tocReference.getChildren().isEmpty()){
                        //子章节递归
                        for (TOCReference child : tocReference.getChildren()) {
                            son_chapterMap.put(child.getTitle(), child.getResourceId());
                        }
                    }
                    chapterMap.put(tocReference.getTitle(),son_chapterMap);
                }

                return new ResponseResult<>(CommonCode.SUCCESS,chapterMap);
            }catch (IOException e){
                e.printStackTrace();
                return new ResponseResult<>(ReadCode.READ_FILE_FAILURE);
            }finally {

            }

        }else{

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
                return new ResponseResult<>(ReadCode.READ_FILE_FAILURE);
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


    }

    @Override  //获取章节
    public ResponseResult getChapterPage(Integer bid,String chapterName){

        Book_Detail book=readMapper.selectById(bid);
        String path=customSecurityProperties.getStaticPath()+book.getSave_path();
        Map<Integer,String> chapterPage=new HashMap<>();

        if(book.getFile_type().equals("epub")){
            String xmlPage="";
            try{
                EpubReader epubReader=new EpubReader();
                Book epubBook=epubReader.readEpub(new FileInputStream(path));
                Resources resources=epubBook.getResources();
                Resource chapterContent =resources.getById(chapterName);
                byte[] data=chapterContent.getData();
                xmlPage=new String(data,chapterContent.getInputEncoding());
            }catch (IOException e){
                e.printStackTrace();
            }

            return new ResponseResult<>(CommonCode.SUCCESS,xmlPage);
        }else{

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
                Book_Detail book=readMapper.selectById(bid);
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
