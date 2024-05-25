/*
 * Adriel Swisher
 * CST 452
 * 
 * Report controller for reports model and associated views
 */
package com.financer.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.financer.persistence.data.ReportDataService;
import com.financer.persistence.model.Customer;
import com.financer.persistence.model.DataModel;
import com.financer.persistence.model.DataRowModel;
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

    @PostMapping("/deleteReport")
    public String deleteReport(@ModelAttribute Report report, Model model, RedirectAttributes redirectAttrs) {
        try {
            ds.deleteReportDataRecords(report);
            rds.delete(report);
            redirectAttrs.addFlashAttribute("deleteMessage", "Report successfully deleted");
        }
        catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Could not delete");
        }
        return "redirect:/reports/manageReports";
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

        if (customerIds.isPresent()) {
            String ids = String.join(", ", customerIds.get());
            report.setCustomerIds(ids);
        }
        
        String revenueType = "";
        if (reveneueTypeId.isPresent() && !reveneueTypeId.get().isEmpty()) {
            revenueType = reveneueTypeId.get();
            report.setTypes(revenueType);
        }

        String expenseType = "";
        if (expenseTypeId.isPresent() && !expenseTypeId.get().isEmpty()) {
            expenseType = expenseTypeId.get();
            report.setTypes(expenseType);
        }

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

        Report r = rds.update(report);
        generateReportData(r);

        List<Report> reports = rds.findAll();
        model.addAttribute("reports", reports);

        return "manageReports";
    }

    @PostMapping("/viewReport")
    public String viewReport(@ModelAttribute Report report, Model model) throws org.json.simple.parser.ParseException {
        Report r = rds.findByReportId(report.getReportId());
        DataModel dm = ds.findDataModelByReportId(r);
        List<DataRowModel> dataRows = ds.findDataRowModelsByDataModel(dm);
        List<JSONObject> jsonData = new ArrayList<>();
        List<String> headings = Arrays.asList(dm.getColumnDefinition().split(","));

        for (DataRowModel d : dataRows) {
            JSONParser js = new JSONParser();
            JSONObject j = (JSONObject) js.parse(d.getDataDetail());
            jsonData.add(j);
        }

        model.addAttribute("headings", headings);
        model.addAttribute("jsonDataList", jsonData);
        model.addAttribute("reportName", r.getName());
        model.addAttribute("report", r);
        return "viewReport";
    }

    @PostMapping("/exportReport")
    public ResponseEntity<Resource> exportReport(@ModelAttribute Report report, Model model) throws IOException, org.json.simple.parser.ParseException {
        Report r = rds.findByReportId(report.getReportId());
        DataModel dm = ds.findDataModelByReportId(r);
        List<DataRowModel> dataRows = ds.findDataRowModelsByDataModel(dm);
        List<JSONObject> jsonData = new ArrayList<>();
        List<String> headings = Arrays.asList(dm.getColumnDefinition().split(","));

        for (DataRowModel d : dataRows) {
            JSONParser js = new JSONParser();
            JSONObject j = (JSONObject) js.parse(d.getDataDetail());
            jsonData.add(j);
        }

        String fileName = r.getCreatedDate().toString() + "_" + r.getName().replace(" ", "_").replace("-", "_") + ".csv";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);

        csvPrinter.printRecord(headings.stream());

        for (JSONObject obj : jsonData) {
            List<String> dataField = new ArrayList<>();
            for (String s : headings) {
                dataField.add(obj.get(s).toString());
            }
            csvPrinter.printRecord(dataField);
        }

        csvPrinter.flush();
        ByteArrayInputStream b = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(b);
        csvPrinter.close();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .contentType(MediaType.parseMediaType("application/csv"))
            .body(file);
    }

    private void generateReportData(Report report) {
        DataModel dm = new DataModel();
        List<TimePeriod> timePeriods = ds.findTimePeriodsByDates(report.getFromPeriod().getFromDate(), report.getToPeriod().getToDate());

        if (report.getReportType().getTypeId() == ds.getCustomerReportType().getTypeId()) {
            List<Customer> cust = cds.findCustomersByIdsIn(report.getCustomerIds().split(", "));

            dm.setReport(report);
            dm.setColumnDefinition("Customer,Revenue Type,Time Period,Total Revenue,Adjustment Amount");
            dm.setCreatedDate(report.getCreatedDate());
            ds.updateData(dm);

            List<JSONObject> js = ds.getRevenueByCustomerAndPeriodIn(cust, timePeriods);

            for (JSONObject j : js) {
                DataRowModel d = new DataRowModel();
                d.setDataDetail(j.toString());
                d.setData(dm);
                ds.updateDataRow(d);
            }
        }
        else if (report.getReportType().getTypeId() == ds.getRevenueReportType().getTypeId()) {
            dm.setReport(report);
            dm.setColumnDefinition("Time Period,Revenue Type,Total Revenue,Adjustment Amount");
            dm.setCreatedDate(report.getCreatedDate());
            ds.updateData(dm);
            List<JSONObject> js = new ArrayList<>();

            if (report.getTypes().isEmpty()) {
                js = ds.getRevenueByTimePeriodsIn(timePeriods);
            }
            else if (!report.getTypes().isEmpty()) {
                List<Type> types = ds.findTypesByIdsAndCategoryIn(report.getTypes().split(", "), "Revenue");
                js = ds.getRevenueByTypeAndPeriodIn(types, timePeriods);
            }

            for (JSONObject j : js) {
                DataRowModel d = new DataRowModel();
                d.setDataDetail(j.toString());
                d.setData(dm);
                ds.updateDataRow(d);
            }
        }
        else if (report.getReportType().getTypeId() == ds.getExpenseReportType().getTypeId()) {
            dm.setReport(report);
            dm.setColumnDefinition("Time Period,Expense Type,Expense Cost,Tax Cost,Total Cost");
            dm.setCreatedDate(report.getCreatedDate());
            ds.updateData(dm);
            List<JSONObject> js = new ArrayList<>();

            if (report.getTypes().isEmpty()) {
                js = ds.getExpenseByTimePeriodsIn(timePeriods);
            }
            else if (!report.getTypes().isEmpty()) {
                List<Type> types = ds.findTypesByIdsAndCategoryIn(report.getTypes().split(", "), "Expense");
                js = ds.getExpenseByTypeAndPeriodIn(types, timePeriods);
            }

            for (JSONObject j : js) {
                DataRowModel d = new DataRowModel();
                d.setDataDetail(j.toString());
                d.setData(dm);
                ds.updateDataRow(d);
            }
        }
        else if (report.getReportType().getTypeId() == ds.getPayrollReportType().getTypeId()) {

            dm.setReport(report);
            dm.setColumnDefinition("Time Period,Pay Date,Hours Worked,Regular Pay,Overtime Pay,Total Pay");
            dm.setCreatedDate(report.getCreatedDate());
            ds.updateData(dm);
            List<JSONObject> js = new ArrayList<>();

            js = ds.getEmployeePayrollByPayDate(report.getFromPeriod().getFromDate(), report.getToPeriod().getToDate());

            for (JSONObject j : js) {
                DataRowModel d = new DataRowModel();
                d.setDataDetail(j.toString());
                d.setData(dm);
                ds.updateDataRow(d);
            }
        }

    }

}
