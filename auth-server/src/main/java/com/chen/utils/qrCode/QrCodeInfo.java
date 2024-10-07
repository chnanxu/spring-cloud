package com.chen.utils.qrCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


/**
 * 二维码信息  存入Redis中
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeInfo {

    /**
     * 二维码id
     */
    private String qrCodeId;
    /**
     * 二维码状态
     * 0:等待扫码；1:已扫码；2:已确认
     */
    private Integer qrCodeStatus;

    /**
     * 二维码过期时间
     */
    private LocalDateTime expiresTime;

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
