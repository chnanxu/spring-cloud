package com.chen.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItem {
    private long rid;
    private String pid;
    private String uid;
    private String uname;
    private String create_time;
    private String report_reason;
    private String report_content;
}
