package com.chen.repository.create;

import com.chen.pojo.page.Posts_Temp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


public interface PostTempRepository extends MongoRepository<Posts_Temp,String> {
    Page<Posts_Temp> findByIsOKOrderByCreateTimeDesc(int isOK,Pageable pageable);

    Page<Posts_Temp> findByIsOKOrderByReadTimesDesc(int isOK,Pageable pageable);

    Page<Posts_Temp> findByIsOKAndUidOrderByCreateTimeDesc(int isOK,String uid, Pageable pageable);

    Page<Posts_Temp> findByIsOKAndUidOrderByReadTimesDesc(int isOK,String uid, Pageable pageable);

    Integer countByUid(String uid);
}
