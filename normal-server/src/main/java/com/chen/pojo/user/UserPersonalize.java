package com.chen.pojo.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_personalize")
public class UserPersonalize {
    private String uid;
    private int type_id;
    private String type_name;
    private long read_times;
    private String lastAccess_time;
}
