package com.chen.pojo.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import pojo.Item_Details;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item_Comments {
    private long comment_id;
    private String content;
    private long like_times;
    private String uid;
    private String uname;
    private boolean isDelete;
    private long pid;
    private long root_commentID;
    private long to_commentID;
    private String update_time;
    private String location;
    private String reuname;

    @Transient
    private int sonCommentCount;

    @Transient
    private List<Item_Comments> sonList;

    @Transient
    private boolean isUserLike;

    @Transient
    private Item_Details item_detail;

}
