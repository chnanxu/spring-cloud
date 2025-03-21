package com.chen.controller;

import com.chen.service.AccountService;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.RedisCache;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import static com.chen.utils.util.RedisConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
 //验证码接口
public class CaptchaController {

    private final Producer producer;

    private final RedisCache redisCache;

    private final AccountService accountService;

    @SneakyThrows
    @RequestMapping ("/captcha/{captchaId}")  //图形验证码
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response,@PathVariable String captchaId){
        response.setContentType("image/jpg");

        String capText=producer.createText();
        log.info("验证码:{}",capText);
        request.getSession().setAttribute("captcha",capText);
        redisCache.setCacheObject((IMAGE_CAPTCHA_PREFIX_KEY +captchaId), capText, DEFAULT_TIMEOUT_SECONDS);
        BufferedImage image=producer.createImage(capText);
        OutputStream out=response.getOutputStream();

        ImageIO.write(image,"jpg",out);

        out.close();
    }

    @ResponseBody
    @GetMapping("/getSmsCaptcha/{phone}")   //手机验证码
    public ResponseResult getSmsCaptcha(@PathVariable("phone") String phone){
        Map<String,Object> result=new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message","获取短信验证码成功");
        result.put("data","1234");
        redisCache.setCacheObject(SMS_CAPTCHA_PREFIX_KEY+phone,"1234");
        return new ResponseResult(CommonCode.SUCCESS,result);
    }

    @ResponseBody
    @GetMapping("/sendEmailCode/{email}")   //邮箱验证码
    public ResponseResult checkSmsCaptcha(@PathVariable String email){
        return accountService.sendEmailCode(email);
    }

}
