package com.chen.service;

import com.chen.pojo.user.Oauth2ThirdAccount;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.User;
import com.chen.utils.result.ResponseResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public interface UserDetailService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User findByName(String username);

    ResponseResult<String> createUserIpLocation(String ip, HttpRequest headers);

    int register(User user);

    Oauth2UserinfoResult getLoginUserInfo();

    String saveByThirdAccount(Oauth2ThirdAccount thirdAccount);

    Oauth2UserinfoResult syncUserLog();
    
}
