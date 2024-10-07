package com.chen.service.oauth2;


import com.chen.utils.qrCode.*;

public interface IQrCodeLoginService {
    /**
     * 生成二维码
     * @return 二维码
     */
    QrCodeGenerateResponse generateQrCode();

    /**
     * 扫描二维码
     * @param loginScan 二维码id
     * @return 二维码信息
     */
    QrCodeLoginScanResponse scan(QrCodeLoginScanRequest loginScan);

    /**
     * 二维码登录确认入参
     * @param loginConsent 二维码id
     */
    void consent(QrCodeLoginConsentRequest loginConsent);

    /**
     * web端轮询二维码状态处理
     * @param qrCodeId 二维码id
     * @return 二维码信息
     */
    QrCodeLoginFetchResponse fetch(String qrCodeId);
}
