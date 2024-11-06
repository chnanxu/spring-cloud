package com.chen.pojo.permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author vains
 * @since 2023-07-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysAuthority implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单自增ID
     */

    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Integer menuPid;

    /**
     * 跳转URL
     */
    private String url;

    /**
     * 所需权限
     */
    private String authority;

    /**
     * 排序
     */
    private Byte sort;

    /**
     * 0:菜单,1:接口
     */
    private Byte type;

    /**
     * 0:启用,1:删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Integer createUserId;
}