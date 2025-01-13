package com.chen.repository.create;

import com.chen.pojo.page.Posts_Takeoff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


public interface PostTakeoffRepository extends MongoRepository<Posts_Takeoff,String> {
    Page<Posts_Takeoff> findByOrderByTakeoffTimeDesc(Pageable pageable);

    Page<Posts_Takeoff> findByOrderByReadTimesDesc(Pageable pageable);

    Page<Posts_Takeoff> findByUidOrderByCreateTimeDesc(String uid, Pageable pageable);

    Page<Posts_Takeoff> findByUidOrderByTakeoffTimeDesc(String uid, Pageable pageable);
}
