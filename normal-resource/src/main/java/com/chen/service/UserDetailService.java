package com.chen.service;

import com.chen.pojo.user.Oauth2ThirdAccount;
import com.chen.pojo.user.Oauth2UserinfoResult;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailService {

//    @Override
//    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Oauth2UserinfoResult getLoginUserInfo();

    String saveByThirdAccount(Oauth2ThirdAccount thirdAccount);

    Oauth2UserinfoResult syncUserLog();
    
}
