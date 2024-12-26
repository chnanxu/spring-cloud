package com.chen.service;

import com.chen.mapper.user.Oauth2BasicUserMapper;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService{

    private final Oauth2BasicUserMapper basicUserMapper;

    @Override
    public ResponseResult getUserBaseInfo(String uid) {

        return new ResponseResult(CommonCode.SUCCESS,basicUserMapper.getUserBaseInfo(uid)) ;
    }
}
