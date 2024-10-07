package com.chen.pojo.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserItem {
    private int item_id;
    private String item_name;
    private String href;
}
