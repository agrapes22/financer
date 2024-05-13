/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.financer.controller;

import com.financer.persistence.data.CustomerDataService;
import com.financer.persistence.model.Customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author gino
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    CustomerDataService cds;

    @PostMapping("/editCustomer")
    public String editCustomer(@ModelAttribute Customer customer, Model model) {
        Customer c = cds.findById(customer.getCustomerId());
        model.addAttribute("customer", c);
        model.addAttribute("id", c.getCustomerId());
        return "editCustomer";
    }

    @GetMapping("/manageCustomers")
    public String getCustomer(Model model) {
        List<Customer> customers = cds.findAll();
        //Customer c = cds.findById(id);
        model.addAttribute("customers", customers);
        return "manageCustomers";
    }

    @GetMapping("/newCustomer") 
    public String newCustomerForm(Model model){
        model.addAttribute("customer", new Customer());
        return "editCustomer";
    }

    @PostMapping("/createCustomer")
    public String customerForm(@ModelAttribute Customer customer, Model model) {
        Customer c = cds.update(customer);
        model.addAttribute("customer", c);
        return "customerEdit";
    }

    @PostMapping("/deleteCustomer")
    public String deleteCustomer(@ModelAttribute Customer customer, Model model) {
        cds.delete(customer);
        return "customers";
    }

    @PostMapping("/edit")
    public String updateCustomer(@ModelAttribute Customer customer, Model model) {
        Customer c = cds.update(customer);
        model.addAttribute("customer", c);
        model.addAttribute("id", c.getCustomerId());
        model.addAttribute("successMessage", "Customer updated!");
        return "editCustomer";
    }
}
