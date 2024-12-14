package com.chen.config;

import com.chen.config.sensitive.MyDdWordAllow;
import com.chen.config.sensitive.MyDdWordDeny;
import com.github.houbb.sensitive.word.api.ISensitiveWordCharIgnore;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.bs.SensitiveWordContext;
import com.github.houbb.sensitive.word.support.allow.WordAllowSystem;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SensitiveWordConfig {

    private final MyDdWordAllow myDdWordAllow;

    private final MyDdWordDeny myDdWordDeny;

    @Bean
    public SensitiveWordBs sensitiveWordBs(){
        SensitiveWordBs sensitiveWord= SensitiveWordBs.newInstance()
                //白名单
                .wordAllow(WordAllows.chains(WordAllowSystem.getInstance(),myDdWordAllow))
                //黑名单
                .wordDeny(WordDenys.chains(WordDenySystem.getInstance(),myDdWordDeny))
                //忽略大小写
                .ignoreCase(true)
                //忽略半角全角
                .ignoreWidth(true)
                //忽略数字的写法
                .ignoreNumStyle(false)
                //忽略中文的书写格式
                .ignoreChineseStyle(true)
                //忽略英文的书写格式
                .ignoreEnglishStyle(true)
                //忽略重复词
                .ignoreRepeat(true)
                //是否启用数字检测，默认连续8位数字则认为是敏感词
                .enableNumCheck(false)
                //是否启用邮箱检测
                .enableEmailCheck(true)
                //是否启用链接检测
                .enableUrlCheck(true)
                .init();
        return sensitiveWord;
    }

    public void refresh(){
        //更新敏感词库
        sensitiveWordBs().init();
    }

}
