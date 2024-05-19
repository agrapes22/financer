/*
 * Adriel Swisher
 * CST 452
 * 
 * Application controller for base URL and login page
 */
package com.financer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.financer.persistence.model.Customer;
import com.financer.persistence.model.User;

@Controller
@RequestMapping("/")
public class ApplicationController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("customer", new Customer());
        return "home.html";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login.html";
    }

    @GetMapping("/denied")
    public String deniedPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("deniedMessage", "Login denied, please try again");
        return "login.html";
    }
}
