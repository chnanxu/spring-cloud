package com.chen.pojo.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_likeuser")
public class User_likeuser {
    private String uid;
    private String like_uid;
}
