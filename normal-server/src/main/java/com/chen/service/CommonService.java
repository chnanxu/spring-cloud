package com.chen.service;

import com.chen.utils.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface CommonService {
    ResponseResult getUserBaseInfo(String uid);
}
