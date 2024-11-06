package com.chen.pojo.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommuninty {
    private String uid;
    private long community_id;
    private String last_sign_time;

}
