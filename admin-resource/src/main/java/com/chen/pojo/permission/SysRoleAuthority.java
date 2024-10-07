package com.chen.pojo.permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleAuthority implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色菜单关联表自增ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 权限菜单ID
     */
    private Integer authorityId;
}