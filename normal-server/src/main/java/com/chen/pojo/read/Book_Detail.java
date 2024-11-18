package com.chen.pojo.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class Book_Detail {
    private Integer bid;
    private String title;
    private String author;
    private String uid;
    private String create_time;
    private String save_path;
}
