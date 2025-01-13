package com.chen.pojo.page;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("posts")
public class Posts {
    private String uid;
    private String uname;

    @Field("_id")
    private String pid;

    private long communityId;
    private String communityName;
    private int typeId;
    private String typeName;
    private String detailType;
    private String title;
    private String simpleText;
    private String content;
    private String coverImg;
    private String href;
    private String tag;
    private String createTime;
    private String updateTime;
    private long readTimes;
    private long likeTimes;
    private long commentsTimes;
    private long favorites;
}
