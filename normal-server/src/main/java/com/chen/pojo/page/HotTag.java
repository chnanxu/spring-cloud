package com.chen.pojo.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
public class HotTag {
    private long tag_id;
    private String tag;
    private long use_times;
    private String create_times;
}
