package com.financer.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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
import com.financer.persistence.data.ReportDataService;
import com.financer.persistence.model.Customer;
import com.financer.persistence.model.Report;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    ReportDataService rds;

    @Autowired
    DataService ds;

    @Autowired
    CustomerDataService cds;

    @GetMapping("/manageReports")
    public String manageReports(Model model) {
        List<Report> reports = rds.findAll();
        model.addAttribute("reports", reports);
        return "manageReports";
    }

    @GetMapping("/newReport")
    public String newReport(Model model) {
        List<TimePeriod> timePeriods = ds.findAllTimePeriods();
        List<Type> revenueTypes = ds.findAllRevenueTypes();
        List<Type> expenseTypes = ds.findAllExpenseTypes();
        List<Customer> customers = cds.findAll();
        model.addAttribute("report", new Report());
        model.addAttribute("timePeriods", timePeriods);
        model.addAttribute("revenueTypes", revenueTypes);
        model.addAttribute("expenseTypes", expenseTypes);
        model.addAttribute("customers", customers);
        return "newReport";
    }

    @PostMapping("/runReport")
    public String runReport(@ModelAttribute Report report, Model model,
            @RequestParam("reportTypeString") String reportTypeString,
            @RequestParam("timePeriodSelect[]") String[] timePeriods,
            @RequestParam("expenseType") Optional<String> expenseTypeId,
            @RequestParam("revenueType") Optional<String> reveneueTypeId,
            @RequestParam("customerIdSelect[]") Optional<String[]> customerIds,
            @RequestParam("createdDateRaw") String createdDate)
    {
        List<Customer> customers;

        if (customerIds.isPresent()) {
            customers = cds.findCustomersByIdsIn(customerIds.get());
            String ids = String.join(", ", customerIds.get());
            report.setCustomerIds(ids);
        }

        //For use when generating report data records
        //String dataType = expenseTypeId.isPresent() ? expenseTypeId.get() : (reveneueTypeId.isPresent() ? reveneueTypeId.get() : "");

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date;
            date = sdf1.parse(createdDate);
            java.sql.Date sqlCreatedDate = new java.sql.Date(date.getTime());
            report.setCreatedDate(sqlCreatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Type reportTypeType = ds.findTypeByNameAndCategory(reportTypeString, "Report");
        report.setReportType(reportTypeType);

        List<TimePeriod> range = ds.findTimePeriodsByIds(timePeriods);

        Collections.sort(range, new Comparator<TimePeriod>() {
            public int compare(TimePeriod o1, TimePeriod o2) {
                return o1.getFromDate().compareTo(o2.getFromDate());
            }
        });

        report.setFromPeriod(range.get(0));
        report.setToPeriod(range.get(range.size() - 1));

        rds.update(report);
        List<Report> reports = rds.findAll();
        model.addAttribute("reports", reports);
        return "manageReports";
    }

}
