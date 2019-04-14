package com.candy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CandyController {

    @GetMapping("/getLogin")
    public String getLogin() {
        return "login";
    }



}
