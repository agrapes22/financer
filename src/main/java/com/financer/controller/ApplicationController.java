/*
 * Adriel Swisher
 * CST 452
 * 
 * Application controller for base URL and login page
 */
package com.financer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.financer.persistence.model.User;

@Controller
@RequestMapping("/")
public class ApplicationController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("usersName", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping("/denied")
    public String deniedPage(Model model) {
        model.addAttribute("deniedMessage", "Username or password incorrect, please try again");
        return "login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "accessDenied";
    }
}
