package com.softserve.edu.jom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login-form")
    public String login() {
        return "login-page";
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "index";
    }

}
