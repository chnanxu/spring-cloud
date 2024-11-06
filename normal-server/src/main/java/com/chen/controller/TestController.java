package com.chen.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test01")
    @PreAuthorize("hasAuthority('message:write')")
    public String test01(){
        return "test01";
    }

    @GetMapping("/test02")
    @PreAuthorize("hasAuthority('test02')")
    public String test02(){
        return "test02";
    }

    @GetMapping("/test03")
    @PreAuthorize("hasAuthority('app')")
    public String test03(){
        return "app";
    }

}
