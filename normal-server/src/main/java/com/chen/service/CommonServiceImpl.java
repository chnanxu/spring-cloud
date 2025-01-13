package com.chen.service;

import com.chen.mapper.CommonMapper;
import com.chen.mapper.user.Oauth2BasicUserMapper;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService{

    private final CommonMapper commonMapper;
    @Override
    public ResponseResult<Map<String,Object>> getUserBaseInfo(String uid) {

        return new ResponseResult<>(CommonCode.SUCCESS,commonMapper.getUserBaseInfo(uid)) ;
    }
}
