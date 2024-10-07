package com.chen.utils.qrCode;

import lombok.Data;

/**
 * 扫描二维码入参
 */
@Data
public class QrCodeLoginScanRequest {

    /**
     * 二维码id
     */
    private String qrCodeId;
}
