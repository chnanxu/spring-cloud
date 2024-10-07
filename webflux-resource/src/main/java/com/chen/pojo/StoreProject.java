package com.chen.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("store_project")
public class StoreProject{
    @TableField("gid")
    private long gid;

    @TableField("name")
    private String name;

    @TableField("simple_text")
    private String simple_text;

    @TableField("content")
    private String content;

    @TableField("cover_img")
    private String cover_img;

    @TableField("rating")
    private float rating;

    @TableField("tag")
    private List<String> tag;

    @TableField("href")
    private String href;

    @TableField("uid")
    private String uid;

    @TableField("uname")
    private String uname;

    @TableField("create_time")
    private String create_time;

    @TableField("sale_times")
    private long sale_times;

    @TableField("isDeleted")
    private boolean isDeleted;

    @TableField("isTakeoff")
    private boolean isTakeoff;
}
