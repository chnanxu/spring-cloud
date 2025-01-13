package com.chen.service.user;

import com.chen.pojo.user.Oauth2ThirdAccount;
import com.chen.pojo.user.Oauth2UserinfoResult;
import com.chen.pojo.user.UserPrivacy;
import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailService {

    Oauth2UserinfoResult getLoginUserInfo();

    Oauth2UserinfoResult syncUserLog();

    ResponseResult<UserPrivacy> getPrivacySetting();
}
