package com.chen.mapper;

import com.chen.pojo.StoreProject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper()
@Repository
public interface StoreMapper {
    List<StoreProject> getStoreIndex();
}
