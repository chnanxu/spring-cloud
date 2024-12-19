package com.chen.pojo.page;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
@TableName("item_details_temp")
public class Item_Details_Temp extends Item_Details{
    private String uid;
    private String uname;

    @TableId
    private String pid;

    private long community_id;
    private String community_name;
    private int type_id;
    private String type_name;
    private String detail_type;
    private String title;
    private String simple_text;
    private String content;
    private String cover_img;
    private String href;
    private String tag;
    private String create_time;
    private long read_times;
    private long like_times;
    private long comments_times;
    private long favorites;
    /*
    * @isOK 0:草稿
    *       1:提交审核
    *       -1:审核不通过被回退
    *
    * */
    private int isOK;
    private String refuse_reason;
}
