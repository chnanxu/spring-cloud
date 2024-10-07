package com.chen.utils.qrCode;

import lombok.Data;

import java.util.Set;

@Data
public class QrCodeLoginScanResponse {

    /**
     * 二维码临时票据
     */
    private String qrCodeTicket;

    /**
     * 二维码状态
     */
    private Integer qrCodeStatus;

    /**
     * 是否过期
     */
    private Boolean expired;

    /**
     * 待确认scope
     */
    private Set<String> scopes;


}
