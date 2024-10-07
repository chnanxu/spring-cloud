package com.chen.utils.qrCode;

import lombok.Data;

/**
 * 二维码登录确认入参
 */
@Data
public class QrCodeLoginConsentRequest {


    /**
     * 二维码id
     */
    private String qrCodeId;

    /**
     * 扫描二维码产生的临时票据（一次有效）
     */
    private String qrCodeTicket;
}
