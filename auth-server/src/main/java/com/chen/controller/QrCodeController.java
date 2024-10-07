package com.chen.controller;


import com.chen.service.oauth2.IQrCodeLoginService;
import com.chen.utils.qrCode.QrCodeLoginConsentRequest;
import com.chen.utils.qrCode.QrCodeLoginScanRequest;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("permitAll()")
@RestController
@RequiredArgsConstructor
@RequestMapping("/qrcode")
public class QrCodeController {

    private final IQrCodeLoginService iQrCodeLoginService;

    //扫码登录------------------------------------------------------------------------------------------------------------

    @GetMapping("/generate") //生成二维码
    public ResponseResult qrCodeLogin(){

        return new ResponseResult(CommonCode.SUCCESS,iQrCodeLoginService.generateQrCode());
    }

    @GetMapping("/fetch/{qrCodeId}")
    public ResponseResult fetch(@PathVariable String qrCodeId){
        return new ResponseResult(CommonCode.SUCCESS,iQrCodeLoginService.fetch(qrCodeId));
    }

    @PostMapping("/scan")
    public ResponseResult scan(@RequestBody QrCodeLoginScanRequest loginScan){

        return new ResponseResult(CommonCode.SUCCESS,iQrCodeLoginService.scan(loginScan));
    }

    @PostMapping("/consent")
    public ResponseResult consent(@RequestBody QrCodeLoginConsentRequest loginConsent){
        iQrCodeLoginService.consent(loginConsent);
        return new ResponseResult(CommonCode.SUCCESS,"登录成功");
    }



}
