package com.chen.pojo.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryUserResult {
    private String uid;
    private String username;
    private String email;
    private String phone;
    private String uname;
    private boolean sex;
    private String user_img;
    private String sign_time;
    private boolean deleted;
    private boolean enabled;

    private String role_name;

}
