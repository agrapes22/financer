/*
 * Adriel Swisher
 * CST 452
 * 
 * User controller for user model, authentication, and associated views
 */
package com.financer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.financer.persistence.data.ReportDataService;
import com.financer.persistence.data.UserAuthDetailsService;
import com.financer.persistence.data.UserDataService;
import com.financer.persistence.model.Report;
import com.financer.persistence.model.User;

@Controller
@RequestMapping("/users")
public class UserController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    UserDataService uds;

    @Autowired
    ReportDataService rds;

    @Autowired
    UserAuthDetailsService auth;

    @GetMapping("/manageUsers")
    public String manageUsers(Model model) {
        List<User> users = uds.findAll();
        if (!model.containsAttribute("users"))
            model.addAttribute("users", users);
        return "manageUsers";
    }

    @GetMapping("/searchUsers")
    public String searchCustomers(@RequestParam("search") String search, Model model, RedirectAttributes redirectAttrs) {
        List<User> users = new ArrayList<>();
        if (search.toLowerCase().contains("admin")) {
            users = uds.findAdminUsers();
        }
        else if (search.toLowerCase().contains("regular")) {
            users = uds.findRegularUsers();
        }
        else {
            users = uds.findUsersBySearchTerm(search);
        }
        redirectAttrs.addFlashAttribute("users", users);
        redirectAttrs.addFlashAttribute("usersSearch", "Results for \"" + search + "\"");

        return "redirect:/users/manageUsers";
    }

    @GetMapping("/clearFilter")
    public String clearFilter(Model model) {
        return "redirect:/users/manageUsers";
    }

    @GetMapping("/newUser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "New User");
        model.addAttribute("is_required", "yes");
        return "editUser";
    }

    @GetMapping("/editUser")
    public String editUser(@ModelAttribute User user, Model model) {
        User u = uds.findById(user.getUserId());
        model.addAttribute("user", u);
        return "editUser";
    }

    @PostMapping("/editUserForm")
    public String editUserForm(@ModelAttribute User user, Model model, RedirectAttributes redirectAttrs) {
        User u = uds.findById(user.getUserId());
        redirectAttrs.addFlashAttribute("user", u);
        return "redirect:/users/editUser";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttrs) {
        User u = new User();
        if (!request.getParameter("newPass").isEmpty() && !request.getParameter("confirmNew").isEmpty()) {
            if (!request.getParameter("newPass").equals(request.getParameter("confirmNew"))) {
                redirectAttrs.addFlashAttribute("errorMessage", "New and confirm password did not match, user not created");
                redirectAttrs.addFlashAttribute("user", user);
                return "redirect:/users/editUser";
            }
            else {
                u = uds.update(user);
                auth.updatePassword(u, request.getParameter("newPass"));
                redirectAttrs.addFlashAttribute("successMessage", "Password succesfully updated!");
            }
        }
        
        redirectAttrs.addFlashAttribute("user", u);
        redirectAttrs.addFlashAttribute("successMessage", "User updated!");
        return "redirect:/users/editUser";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttrs) {
        try {
            uds.delete(user);
            redirectAttrs.addFlashAttribute("deleteMessage", "User succesfully deleted");
        } catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage", "User could not be deleted");
        }
        return "redirect:/users/manageUsers";
    }

    @PostMapping("/login")
    public String login(Model model) {
        List<Report> reports = rds.findAll();
        model.addAttribute("reports", reports);
        return "manageReports";
    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        User u = uds.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        model.addAttribute("user", u);
        return "account";
    }

    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttrs) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!request.getParameter("oldPass").isEmpty() && !request.getParameter("newPass").isEmpty() && request.getParameter("confirmNew").isEmpty()) {
            
            if (!auth.checkIfOldPasswordValid(u, request.getParameter("oldPass"))) {
                redirectAttrs.addFlashAttribute("errorMessage", "Old password was not correct, password not updated");
                return "redirect:/account";
            }
            else if (!request.getParameter("newPass").equals(request.getParameter("confirmNew"))) {
                redirectAttrs.addFlashAttribute("errorMessage", "New and confirm password did not match, password not updated");
                return "redirect:/account";
            }
            else {
                auth.updatePassword(u, request.getParameter("newPass"));
                redirectAttrs.addFlashAttribute("successMessage", "Password succesfully updated!");
            }
        }

        if (!u.getName().equals(user.getName())) {
            int updated = uds.updateName(user);
            if (updated == 1) {
                redirectAttrs.addFlashAttribute("successMessage", "Account updated");
            }
            else {
                redirectAttrs.addFlashAttribute("errorMessage", "Error! Account couldn't be updated");
            }
        }

        return "redirect:/users/account";
    }
}
