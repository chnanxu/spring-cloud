package com.chen.service;

import com.chen.pojo.page.Item_Details;
import com.chen.pojo.user.Oauth2UserinfoResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {
    Oauth2UserinfoResult getUserInfo(String keywords);

    List<Item_Details> getUserProject(String keywords);
}
