package com.chen.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.chen.service.oauth2.SendEmailService;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.result.UserCode;
import com.chen.utils.util.RedisCache;
import com.github.houbb.heaven.util.lang.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.chen.utils.util.RedisConstants.EMAIL_CODE_KEY;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final SendEmailService emailService;


    private final RedisCache redisCache;

    @Value("${custom.emailCode.expiration}")
    private Long expiration;

    @Override
    public ResponseResult sendEmailCode(String email){

        String emailCodeKey=EMAIL_CODE_KEY+email;

        String cacheCode=redisCache.getCacheObject(emailCodeKey);
        String result_code="";
        if(StringUtil.isBlank(cacheCode)){
            result_code=RandomUtil.randomNumbers(6);
            //存入redis中
            redisCache.setCacheObject(emailCodeKey,result_code);
            redisCache.expire(emailCodeKey,expiration*100);
        }else{
            //缓存中已经存在数据,不用重复生成,或是用户操作太频繁导致
            return new ResponseResult(UserCode.SEND_EMAIL_SUCCESS);
        }


        TemplateEngine engine= TemplateUtil.createEngine(new TemplateConfig("templates",TemplateConfig.ResourceMode.CLASSPATH));
        Template template=engine.getTemplate("email-code.ftl");

        String subject="邮箱验证码";

        String content=template.render(Dict.create().set("code", result_code).set("expiration",expiration));
        emailService.sendAuthCodeEmail(email,subject,content);

        return new ResponseResult(UserCode.SEND_EMAIL_SUCCESS);
    }
}
