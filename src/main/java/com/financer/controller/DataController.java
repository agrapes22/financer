/* 
 * Adriel Swisher
 * CST 452
 * 
 * Data controller for data entry, and administrative types and time periods
*/
package com.financer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.financer.persistence.data.CustomerDataService;
import com.financer.persistence.data.DataService;
import com.financer.persistence.model.Customer;
import com.financer.persistence.model.Expense;
import com.financer.persistence.model.Revenue;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    DataService ds;

    @Autowired
    CustomerDataService cds;

    @GetMapping("/manageTypes")
    public String manageTypes(Model model) {
        List<Type> types = ds.findAllTypes();
        model.addAttribute("types", types);
        return "manageTypes";
    }

    @GetMapping("/newType")
    public String newTypes(Model model, RedirectAttributes redirectAttrs) {
        List<Type> types = ds.findAllTypes();

        redirectAttrs.addFlashAttribute("types", types);
        redirectAttrs.addFlashAttribute("typeEdit", new Type());
        redirectAttrs.addFlashAttribute("editType", "yes");

        return "redirect:/data/manageTypes";
    }

    @PostMapping("/editTypeForm")
    public String editTypeForm(@ModelAttribute Type type, Model model, RedirectAttributes redirectAttrs) {
        Type t = ds.findTypeById(type.getTypeId());
        List<Type> types = ds.findAllTypes();

        redirectAttrs.addFlashAttribute("types", types);
        redirectAttrs.addFlashAttribute("typeEdit", t);
        redirectAttrs.addFlashAttribute("editType", "yes");

        return "redirect:/data/manageTypes";
    }

    @PostMapping("/editType")
    public String editType(@ModelAttribute("typeEdit") Type type, Model model, RedirectAttributes redirectAttrs) {
        ds.updateType(type);
        List<Type> types = ds.findAllTypes();
        redirectAttrs.addFlashAttribute("types", types);
        redirectAttrs.addFlashAttribute("successMessage", "Type successfully updated");
        return "redirect:/data/manageTypes";
    }

    @PostMapping("/deleteType")
    public String deleteType(@ModelAttribute Type type, Model model, RedirectAttributes redirectAttrs) {
        try {
            ds.deleteType(type);
            redirectAttrs.addFlashAttribute("deleteMessage", "Type successfully deleted");
        }
        catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Type is used on records, could not delete");
        }

        return "redirect:/data/manageTypes";
    }

    @GetMapping("/manageTimePeriods")
    public String manageTimePeriods(Model model) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        model.addAttribute("timePeriods", timePeriods);
        return "manageTimePeriods";
    }

    @GetMapping("/newTimePeriod")
    public String newTimePeriod(Model model, RedirectAttributes redirectAttrs) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();

        redirectAttrs.addFlashAttribute("timePeriods", timePeriods);
        redirectAttrs.addFlashAttribute("timePeriodEdit", new TimePeriod());
        redirectAttrs.addFlashAttribute("editTimePeriod", "yes");

        return "redirect:/data/manageTimePeriods";
    }

    @PostMapping("/editTimePeriodForm")
    public String editTimePeriodForm(@ModelAttribute TimePeriod timePeriod, Model model, RedirectAttributes redirectAttrs) {
        TimePeriod t = ds.findTimePeriodById(timePeriod.getTimePeriodId());
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        redirectAttrs.addFlashAttribute("timePeriods", timePeriods);
        redirectAttrs.addFlashAttribute("timePeriodEdit", t);
        redirectAttrs.addFlashAttribute("editTimePeriod", "yes");
        return "redirect:/data/manageTimePeriods";
    }

    @PostMapping("/editTimePeriod")
    public String editTimePeriod(@ModelAttribute("timePeriodEdit") TimePeriod timePeriod, Model model, RedirectAttributes redirectAttrs) {
        ds.updateTimePeriod(timePeriod);
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        redirectAttrs.addFlashAttribute("timePeriods", timePeriods);
        redirectAttrs.addFlashAttribute("successMessage", "Time Period successfully updated");
        return "redirect:/data/manageTimePeriods";
    }

    @PostMapping("/deleteTimePeriod")
    public String deleteTimePeriod(@ModelAttribute TimePeriod timePeriod, Model model, RedirectAttributes redirectAttrs) {
        try {
            ds.deleteTimePeriod(timePeriod);
            redirectAttrs.addFlashAttribute("deleteMessage", "Time Period successfully deleted");
        }
        catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Time Period is used on records, could not delete");
        }

        return "redirect:/data/manageTimePeriods";
    }

    @GetMapping("/enterData")
    public String dataEntryForm(Model model) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        List<Type> adjustmentReasons = ds.findAllAdjReasons();
        List<Type> expenseTypes = ds.findAllExpenseTypes();
        List<Type> revenueTypes = ds.findAllRevenueTypes();
        List<Customer> customers = cds.findAll();

        model.addAttribute("expense", new Expense());
        model.addAttribute("timePeriods", timePeriods);
        model.addAttribute("revenue", new Revenue());
        model.addAttribute("adjTypes", adjustmentReasons);
        model.addAttribute("expenseTypes", expenseTypes);
        model.addAttribute("revenueTypes", revenueTypes);
        model.addAttribute("customers", customers);

        return "dataEntry";
    }

    private void populateDataEntryForm(RedirectAttributes redirectAttrs) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        List<Type> adjustmentReasons = ds.findAllAdjReasons();
        List<Type> expenseTypes = ds.findAllExpenseTypes();
        List<Type> revenueTypes = ds.findAllRevenueTypes();
        List<Customer> customers = cds.findAll();

        redirectAttrs.addFlashAttribute("expense", new Expense());
        redirectAttrs.addFlashAttribute("timePeriods", timePeriods);
        redirectAttrs.addFlashAttribute("revenue", new Revenue());
        redirectAttrs.addFlashAttribute("adjTypes", adjustmentReasons);
        redirectAttrs.addFlashAttribute("expenseTypes", expenseTypes);
        redirectAttrs.addFlashAttribute("revenueTypes", revenueTypes);
        redirectAttrs.addFlashAttribute("customers", customers);
    }

    @PostMapping("/dataEntry")
    public String enterData(@ModelAttribute Expense expense,
                            @ModelAttribute Revenue revenue,
                            @RequestParam("dataTypeString") String dataTypeString,
                            @RequestParam("timePeriodSelectExpense") Optional<String> timePeriodExpense,
                            @RequestParam("timePeriodSelectRevenue") Optional<String> timePeriodRevenue,
                            @RequestParam("adjustmentSelect") Optional<String> adjReason,
                            @RequestParam("revenueTypeSelect") Optional<String> revenueTypeSelect,
                            @RequestParam("expenseTypeSelect") Optional<String> expenseTypeSelect,
                            @RequestParam("customerSelect") Optional<String> customer,
                            Model model,
                            RedirectAttributes redirectAttrs)
    {
        if (dataTypeString.equals("Revenue")) {
            TimePeriod timePeriod = ds.findTimePeriodById(Long.parseLong(timePeriodRevenue.get()));
            revenue.setTimePeriod(timePeriod);

            Type t = ds.findTypeByNameAndCategory(dataTypeString, "Revenue");
            revenue.setType(t);

            if (adjReason.isPresent() && !adjReason.get().equals("")) {
                Type type = ds.findTypeById(Long.parseLong(adjReason.get()));
                revenue.setAdjustmentReason(type);
            }

            Type revType = ds.findTypeById(Long.parseLong(revenueTypeSelect.get()));
            revenue.setType(revType);

            Customer c = cds.findById(Long.parseLong(customer.get()));
            revenue.setCustomer(c);

            Revenue r = ds.updateRevenue(revenue);
            redirectAttrs.addFlashAttribute("revenue", r);
            redirectAttrs.addFlashAttribute("expense", new Expense());
        }
        else if (dataTypeString.equals("Expense")) {
            TimePeriod timePeriod = ds.findTimePeriodById(Long.parseLong(timePeriodExpense.get()));
            expense.setTimePeriod(timePeriod);

            Type t = ds.findTypeByNameAndCategory(dataTypeString, "Expense");
            expense.setType(t);

            Type expType = ds.findTypeById(Long.parseLong(expenseTypeSelect.get()));
            expense.setType(expType);

            Expense e = ds.updateExpense(expense);
            redirectAttrs.addFlashAttribute("revenue", new Revenue());
            redirectAttrs.addFlashAttribute("expense", e);
        }

        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        List<Type> adjustmentReasons = ds.findAllAdjReasons();

        redirectAttrs.addFlashAttribute("timePeriods", timePeriods);
        redirectAttrs.addFlashAttribute("adjTypes", adjustmentReasons);
        redirectAttrs.addFlashAttribute("successMessage", "Data added!");

        populateDataEntryForm(redirectAttrs);
        return "redirect:/data/enterData";
    }

}
