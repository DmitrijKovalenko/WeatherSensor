package com.dimakovalenko.weathersensorrestapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // @Controller +@ResposeBody(означает что этот метод будет возвращать данные// ) над каждым методом
@RequestMapping("/api")
public class FirstRestController {


    @GetMapping("/sayHello")
    public String sayHello(){
        return "Work harder to became a Java Developer";
    }

}
