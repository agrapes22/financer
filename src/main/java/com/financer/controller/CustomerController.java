/*
 * Adriel Swisher
 * CST 452
 * 
 * Customer controller for customer model and related views
 */

package com.financer.controller;

import com.financer.persistence.data.CustomerDataService;
import com.financer.persistence.model.Customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getCustomer(Model model, RedirectAttributes redirectAttrs) {
        List<Customer> customers = cds.findAll();

        if (!model.containsAttribute("customers")) {
            model.addAttribute("customers", customers);
        }

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
    public String deleteCustomer(@ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttrs) {
        try {
            cds.delete(customer);
            redirectAttrs.addFlashAttribute("deleteMessage", "Customer successfully deleted");
        } catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Customer has revenue records associated, cannot delete");
        }
        return "redirect:/customers/manageCustomers";
    }

    @PostMapping("/edit")
    public String updateCustomer(@ModelAttribute Customer customer, Model model) {
        Customer c = cds.update(customer);
        model.addAttribute("customer", c);
        model.addAttribute("id", c.getCustomerId());
        model.addAttribute("successMessage", "Customer updated!");
        return "editCustomer";
    }

    @GetMapping("/searchCustomers")
    public String searchCustomers(@RequestParam("search") String search, Model model, RedirectAttributes redirectAttrs) {
        List<Customer> customers = new ArrayList<>();
        
        if (search.matches("[0-9]+")) {
            Long searchId = Long.parseLong(search);
            Customer c = cds.findById(searchId);
            if (c != null)
                customers.add(c);
        }

        if (customers.isEmpty()) {
            customers = cds.findCustomersBySearchTerm(search);
        }

        redirectAttrs.addFlashAttribute("customers", customers);
        redirectAttrs.addFlashAttribute("customerSearch", "Results for \"" + search + "\"");

        return "redirect:/customers/manageCustomers";
    }

    @GetMapping("/clearFilter")
    public String clearFilter(Model model) {
        return "redirect:/customers/manageCustomers";
    }
}
