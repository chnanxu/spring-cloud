package com.chen.pojo.record;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
public class Diary_Book {
    private String uid;
    private long bid;
    private String b_name;
}
