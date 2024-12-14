package com.chen.service.oauth2;

public interface SendEmailService {
    void sendAuthCodeEmail(String email,String subject,String content);
}
