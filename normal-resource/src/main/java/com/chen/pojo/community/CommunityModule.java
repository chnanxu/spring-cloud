package com.chen.pojo.community;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityModule {
    private int module_id;
    private String module_name;
    private long community_id;
    private String community_name;
    private int height;
    private int width;
}
