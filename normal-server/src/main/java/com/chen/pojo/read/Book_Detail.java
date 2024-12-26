package com.chen.pojo.read;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName("book_detail")
public class Book_Detail {
    private Integer bid;
    private String title;
    private String author;
    private String uid;
    private String create_time;
    private String save_path;
    private String cover_img;
    private String file_type;
}
