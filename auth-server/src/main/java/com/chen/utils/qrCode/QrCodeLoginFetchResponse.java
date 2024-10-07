package com.chen.utils.qrCode;


import lombok.Data;

import java.util.Set;

@Data
public class QrCodeLoginFetchResponse {
    /**
     * 二维码状态
     * 0:等待扫码；1:已扫码；2:已确认
     */
    private Integer qrCodeStatus;

    /**
     * 是否已过期
     */
    private Boolean expired;

    /**
     * 扫码人头像
     */
    private String avatarUrl;

    /**
     * 扫码人用户名
     */
    private String uname;

    /**
     * 需要确认的scope
     */
    private Set<String> scopes;

    /**
     * 跳转登录之前请求的接口
     */
    private String beforeLoginRequestUri;

    /**
     * 跳转登录之前请求参数
     */
    private String beforeLoginQueryString;
}
