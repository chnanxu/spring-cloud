package com.chen.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin_Left_Navbar {

    private int id;
    private String name;
    private String href;
    private int root_id;

    @Transient
    private List<Admin_Left_Navbar> sonList;

}
