package com.chen.pojo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


@Data
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority, Serializable {

    private String authority;

    @Override
    public String getAuthority(){
        return this.authority;
    }

}
