package com.chen.pojo.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeDetails {
    private String uid;
    private long pid;
    private String like_time;
}
