package com.chen.controller;

import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.RedisCache;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.chen.utils.util.RedisConstants.*;

@Slf4j
@Controller
@RequiredArgsConstructor
 //验证码接口
public class CaptchaController {

    private final Producer producer;

    private final RedisCache redisCache;
    @SneakyThrows
    @RequestMapping ("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("image/jpeg");

        String capText=producer.createText();
        log.info("验证码:{}",capText);
        request.getSession().setAttribute("captcha",capText);
        redisCache.setCacheObject((IMAGE_CAPTCHA_PREFIX_KEY +"captchaId"), capText, DEFAULT_TIMEOUT_SECONDS);
        BufferedImage image=producer.createImage(capText);
        OutputStream out=response.getOutputStream();



        ImageIO.write(image,"jpg",out);
    }

    @ResponseBody
    @GetMapping("/getSmsCaptcha/{phone}")
    public ResponseResult getSmsCaptcha(@PathVariable("phone") String phone){
        Map<String,Object> result=new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message","获取短信验证码成功");
        result.put("data","1234");
        redisCache.setCacheObject(SMS_CAPTCHA_PREFIX_KEY+phone,"1234");
        return new ResponseResult(CommonCode.SUCCESS,result);
    }

//    @PostMapping("/checkSmsCaptcha/{phone}")
//    public ResponseResult checkSmsCaptcha(){
//
//    }

}
