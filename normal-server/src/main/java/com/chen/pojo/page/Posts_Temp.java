package com.chen.pojo.page;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("posts_temp")
public class Posts_Temp extends Posts{

    /*
    * @isOK 0:草稿
    *       1:提交审核
    *       -1:审核不通过被回退
    * */
    private int isOK;
    private String refuseReason;
}
