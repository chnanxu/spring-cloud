package com.chen.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CaptchaConfig {
    @Bean
    public Producer producer(){
        Properties properties=new Properties();
        properties.setProperty("kaptcha.img.width","100");
        properties.setProperty("kaptcha.img.height","50");
        properties.setProperty("kaptcha.border.color","239,239,239");


        properties.setProperty("kaptcha.textproducer.char.string","0123456789abcdefghijklmnopqrstuvwxyz");
        properties.setProperty("kaptcha.textproducer.char.length","4");

        Config config=new Config(properties);
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
