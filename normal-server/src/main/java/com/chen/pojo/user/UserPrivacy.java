package com.chen.pojo.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_privacy")
public class UserPrivacy {
    private String uid;
    private boolean hide_email;
    private boolean hide_sex;
    private boolean hide_homepage;
    private boolean receive_message;
}
