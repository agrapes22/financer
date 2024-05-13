package com.financer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.financer.persistence.data.UserDataService;
import com.financer.persistence.model.User;

@Controller
@RequestMapping("/users")
public class UserController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    UserDataService uds;

    @GetMapping("/manageUsers")
    public String manageUsers(Model model) {
        List<User> users = uds.findAll();
        model.addAttribute("users", users);
        return "manageUsers";
    }

    @GetMapping("/newUser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "editUser";
    }

    @PostMapping("/editUser")
    public String editUserForm(@ModelAttribute User user, Model model) {
        User u = uds.findById(user.getUserId());
        model.addAttribute("user", u);
        return "editUser";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, Model model) {
        User u = uds.update(user);
        model.addAttribute("user", u);
        model.addAttribute("successMessage", "User updated!");
        return "editUser";
    }
}
