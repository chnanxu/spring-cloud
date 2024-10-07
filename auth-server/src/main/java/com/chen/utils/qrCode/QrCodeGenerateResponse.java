package com.chen.utils.qrCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeGenerateResponse {

    private String qrCodeId;

    private String imageData;
}
