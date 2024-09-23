package com.zlyntlab.ondabi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class DemoController{

    @GetMapping ("/ping")
    public String ping(){
        return "pong";
    }
}