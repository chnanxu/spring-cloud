package com.chen.service;

import com.chen.mapper.RecordMapper;
import com.chen.pojo.page.Item_Details;
import com.chen.pojo.user.Oauth2UserinfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;

    private final UserDetailService userDetailService;

    @Override
    public Oauth2UserinfoResult getUserInfo(String keywords) {
        if(keywords==null){

        }else{
            return recordMapper.getUserInfo(keywords);
        }
        return null;
    }

    @Override
    public List<Item_Details> getUserProject(String keywords) {

        if(keywords==null){
           Oauth2UserinfoResult user= userDetailService.getLoginUserInfo();
            if(user==null){

            }else{
                return recordMapper.getUserProject(user.getUid());
            }
        }else{
            return recordMapper.getUserProject(keywords);
        }

        return null;
    }
}
