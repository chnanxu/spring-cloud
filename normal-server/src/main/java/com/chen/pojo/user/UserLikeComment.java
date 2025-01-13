package com.chen.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeComment {
    private String uid;
    private String pid;
    private long commentId;
}
