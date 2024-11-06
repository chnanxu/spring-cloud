package com.chen.pojo.page;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Left_NavbarItem implements Serializable {
    private long item_id;
    private String item_name;
    private String item_ico;
    private String type;

}
