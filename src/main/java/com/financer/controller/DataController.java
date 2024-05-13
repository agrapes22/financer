package com.financer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String newTypes(Model model) {
        List<Type> types = ds.findAllTypes();
        model.addAttribute("types", types);
        model.addAttribute("typeEdit", new Type());
        model.addAttribute("editType", "yes");
        return "manageTypes";
    }

    @PostMapping("/editTypeForm")
    public String editTypeForm(@ModelAttribute Type type, Model model) {
        Type t = ds.findTypeById(type.getTypeId());
        List<Type> types = ds.findAllTypes();
        model.addAttribute("types", types);
        model.addAttribute("typeEdit", t);
        model.addAttribute("editType", "yes");
        return "manageTypes";
    }

    @PostMapping("/editType")
    public String editType(@ModelAttribute("typeEdit") Type type, Model model) {
        ds.updateType(type);
        List<Type> types = ds.findAllTypes();
        model.addAttribute("types", types);
        return "manageTypes";
    }

    @GetMapping("/manageTimePeriods")
    public String manageTimePeriods(Model model) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        model.addAttribute("timePeriods", timePeriods);
        return "manageTimePeriods";
    }

    @GetMapping("/newTimePeriod")
    public String newTimePeriod(Model model) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        model.addAttribute("timePeriods", timePeriods);
        model.addAttribute("timePeriodEdit", new TimePeriod());
        model.addAttribute("editTimePeriod", "yes");
        return "manageTimePeriods";
    }

    @PostMapping("/editTimePeriodForm")
    public String editTimePeriodForm(@ModelAttribute TimePeriod timePeriod, Model model) {
        TimePeriod t = ds.findTimePeriodById(timePeriod.getTimePeriodId());
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        model.addAttribute("timePeriods", timePeriods);
        model.addAttribute("timePeriodEdit", t);
        model.addAttribute("editTimePeriod", "yes");
        return "manageTimePeriods";
    }

    @PostMapping("/editTimePeriod")
    public String editTimePeriod(@ModelAttribute("timePeriodEdit") TimePeriod timePeriod, Model model) {
        ds.updateTimePeriod(timePeriod);
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        model.addAttribute("timePeriods", timePeriods);
        return "manageTimePeriods";
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
                            Model model)
    {
        if (dataTypeString.equals("Revenue")) {
            TimePeriod timePeriod = ds.findTimePeriodById(Long.parseLong(timePeriodRevenue.get()));
            System.out.println("Time period: " + timePeriod.getDisplayPeriod());
            revenue.setTimePeriod(timePeriod);

            Type t = ds.findTypeByNameAndCategory(dataTypeString, "Revenue");
            revenue.setType(t);

            if (adjReason.isPresent()) {
                Type type = ds.findTypeById(Long.parseLong(adjReason.get()));
                revenue.setAdjustmentReason(type);
            }

            Type revType = ds.findTypeById(Long.parseLong(revenueTypeSelect.get()));
            revenue.setType(revType);

            Customer c = cds.findById(Long.parseLong(customer.get()));
            revenue.setCustomer(c);

            Revenue r = ds.updateRevenue(revenue);
            model.addAttribute("revenue", r);
            model.addAttribute("expense", new Expense());
        }
        else if (dataTypeString.equals("Expense")) {
            TimePeriod timePeriod = ds.findTimePeriodById(Long.parseLong(timePeriodExpense.get()));
            System.out.println("Time period: " + timePeriod.getDisplayPeriod());
            expense.setTimePeriod(timePeriod);

            Type t = ds.findTypeByNameAndCategory(dataTypeString, "Expense");
            expense.setType(t);

            Type expType = ds.findTypeById(Long.parseLong(expenseTypeSelect.get()));
            expense.setType(expType);

            Expense e = ds.updateExpense(expense);
            model.addAttribute("expense", e);
            model.addAttribute("revenue", new Revenue());
        }

        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        List<Type> adjustmentReasons = ds.findAllAdjReasons();
        model.addAttribute("timePeriods", timePeriods);
        model.addAttribute("adjTypes", adjustmentReasons);

        dataEntryForm(model);
        return "dataEntry";
    }

}
