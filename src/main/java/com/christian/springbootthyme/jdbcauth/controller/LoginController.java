package com.christian.springbootthyme.jdbcauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(){
        return "fancy-login";
    }

    //request for /accessdenied
    @GetMapping("/access-denied")
    public String showAccessDenied(){
        return "access-denied";
    }
}
