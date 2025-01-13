package com.chen.repository.create;

import com.chen.pojo.page.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface PostRepository extends MongoRepository<Posts, String> {

    //根据类型id查找发布时间降序的作品
    Page<Posts> findByTypeIdOrderByCreateTimeDesc(Integer typeId, Pageable pageable);
    //根据发布时间降序查找作品
    Page<Posts> findByOrderByCreateTimeDesc(Pageable pageable);
    //根据访问量降序查找作品
    Page<Posts> findByOrderByReadTimesDesc(Pageable pageable);
    //根据uid查找发布时间降序的作品
    Page<Posts> findByUidOrderByCreateTimeDesc(String uid, Pageable pageable);
    //根据uid查找访问量降序的作品
    Page<Posts> findByUidOrderByReadTimesDesc(String uid, Pageable pageable);
    //统计用户作品数量
    Integer countByUid(String uid);
    Page<Posts> findByUidAndPidNotOrderByCreateTimeDesc(String uid, String pid, Pageable pageable);
}
