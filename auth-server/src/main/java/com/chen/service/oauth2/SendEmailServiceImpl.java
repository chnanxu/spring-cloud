package com.chen.service.oauth2;

import com.chen.utils.util.RedisCache;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService{


    private final JavaMailSender mailSender;

    @Value("${spring.mail.email}")
    private String email;

    @Override
    public void sendAuthCodeEmail(String to_email,String subject,String content){

        if(email==null){
            throw new RuntimeException("邮箱配置异常");
        }
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=null;

        try{
            helper=new MimeMessageHelper(message,true);
            //设置发件人邮箱
            helper.setFrom(email);
            //设置收件人信息
            helper.setTo(to_email);
            helper.setSubject(subject);
            helper.setText(content,true);

        }catch (MessagingException e){
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }
}
