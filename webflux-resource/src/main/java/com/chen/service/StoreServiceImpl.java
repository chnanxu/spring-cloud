package com.chen.service;

import com.chen.mapper.StoreMapper;
import com.chen.pojo.StoreProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final StoreMapper storeMapper;

    @Override
    public List<StoreProject> getStoreIndex() {
        return storeMapper.getStoreIndex();
    }
}
