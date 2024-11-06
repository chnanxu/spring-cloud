package com.chen.pojo.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class My_Earnings {
    private String uid;
    private long pid;
    private float amount;
    private String time;
}
