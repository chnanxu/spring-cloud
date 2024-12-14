package com.chen.pojo.user;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID=1L;
    @TableId
    private String uid;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String uname;
    private byte sex;
    private String user_img;
    private String sign_time;
    private byte deleted;
    private String sourceFrom;
    private boolean enabled;
    private String location;
    private int level;
    private int exp_point;
    private int subscription_count;
    private boolean clock;
    private float balance;

    @TableField(exist = false)
    private String captcha;

    @TableField(exist = false)
    private Collection<CustomGrantedAuthority> authorities;

    @Override
    public Collection<CustomGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.deleted != 1;
    }
}
