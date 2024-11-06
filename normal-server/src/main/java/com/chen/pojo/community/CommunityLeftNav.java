package com.chen.pojo.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityLeftNav {
    private long community_id;
    private String community_name;
    private int left_nav_id;
    private String left_nav_item;
    private String href;
}
