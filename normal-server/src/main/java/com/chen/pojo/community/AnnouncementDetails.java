package com.chen.pojo.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDetails {
    private long announcement_id;
    private long community_id;
    private String community_name;
    private int type_id;
    private String type_name;
    private String content;
    private String href;
    private String create_time;
    private long read_times;
    private long like_times;
    private long collection_times;
}
