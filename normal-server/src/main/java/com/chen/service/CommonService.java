package com.chen.service;

import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CommonService {
    ResponseResult<Map<String,Object>> getUserBaseInfo(String uid);
}
