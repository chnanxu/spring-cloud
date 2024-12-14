package com.chen.config.sensitive;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyDdWordAllow implements IWordAllow {
    @Override
    public List<String> allow(){
        List<String> list=new ArrayList<>();
        list.add(".");

        return list;
    }
}
