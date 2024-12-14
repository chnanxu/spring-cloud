package com.chen.config.sensitive;


import com.github.houbb.sensitive.word.api.IWordDeny;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyDdWordDeny implements IWordDeny {

    @Override
    public List<String> deny(){
        List<String> list =new ArrayList<>();


        return list;
    }
}
