package com.financer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.financer.persistence.model.Customer;

@Controller
@RequestMapping("/")
public class ApplicationController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("customer", new Customer());
        return "home.html";
    }

}
