package com.chen.service;


import com.chen.pojo.StoreProject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoreService {
    List<StoreProject> getStoreIndex();
}
