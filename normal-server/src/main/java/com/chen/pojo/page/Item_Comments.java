package com.chen.pojo.page;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item_Comments {
    private long commentId;
    private String content;
    private long likeTimes;
    private String uid;
    private String uname;
    private boolean isDelete;
    private String pid;
    private long rootCommentID;
    private long toCommentID;
    private String updateTime;
    private String location;
    private String reuname;
    private String reuid;

    @TableField(exist = false)
    private int sonCommentCount;

    @TableField(exist = false)
    private List<Item_Comments> sonList;

    @TableField(exist = false)
    private boolean isUserLike;

    @TableField(exist = false)
    private Posts item_detail;

}
