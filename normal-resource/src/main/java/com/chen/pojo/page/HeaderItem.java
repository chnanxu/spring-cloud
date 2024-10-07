package com.chen.pojo.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderItem {
    private int hid;
    private String name;
    private String href;
}
