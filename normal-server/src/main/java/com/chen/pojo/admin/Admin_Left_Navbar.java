package com.chen.pojo.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin_Left_Navbar {

    private int id;
    private String name;
    private String href;
    private int root_id;

    @TableField(exist = false)
    private List<Admin_Left_Navbar> sonList;

}
