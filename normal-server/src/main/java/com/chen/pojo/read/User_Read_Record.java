package com.chen.pojo.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
@AllArgsConstructor
public class User_Read_Record {
    private String uid;
    private Integer bid;
    private String book_title;
    private String chapter_name;
    private Integer start_page;
    private Integer end_page;
    private String access_time;
}
