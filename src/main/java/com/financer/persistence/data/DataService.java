/*
 * Adriel Swisher
 * CST 452
 * 
 * Data service for database access for data models, including types, time periods, expense, and revenue
 */
package com.financer.persistence.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.Customer;
import com.financer.persistence.model.DataModel;
import com.financer.persistence.model.DataRowModel;
import com.financer.persistence.model.EmployeePayroll;
import com.financer.persistence.model.Expense;
import com.financer.persistence.model.Report;
import com.financer.persistence.model.Revenue;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;
import com.financer.persistence.repo.DataRepository;
import com.financer.persistence.repo.DataRowRepository;
import com.financer.persistence.repo.EmployeePayrollRepository;
import com.financer.persistence.repo.ExpenseRepository;
import com.financer.persistence.repo.RevenueRepository;
import com.financer.persistence.repo.TimePeriodRepository;
import com.financer.persistence.repo.TypeRepository;

@Service
public class DataService {

    @Autowired
    TypeRepository tr;

    @Autowired
    TimePeriodRepository tpr;

    @Autowired
    ExpenseRepository er;

    @Autowired
    RevenueRepository revr;

    @Autowired
    DataRepository dr;

    @Autowired
    DataRowRepository drr;

    @Autowired
    EmployeePayrollRepository empr;

    public List<Type> findAllTypes() {
        return tr.findAll();
    }

    public Type findTypeById(Long typeId) {
        return tr.findByTypeId(typeId);
    }

    public Type updateType(Type t) {
        return tr.save(t);
    }
    
    public Type getCustomerReportType() {
        return tr.findCustomerReportType();
    }

    public Type getRevenueReportType() {
        return tr.findRevenueReportType();
    }

    public Type getExpenseReportType() {
        return tr.getExpenseReportType();
    }

    public Type getPayrollReportType() {
        return tr.getPayrollReportType();
    }

    public List<TimePeriod> findAllTimePeriods() {
        return tpr.findAll();
    }

    public TimePeriod findTimePeriodById(Long timePeriodId) {
        return tpr.findByTimePeriodId(timePeriodId);
    }

    public TimePeriod updateTimePeriod(TimePeriod timePeriod) {
        return tpr.save(timePeriod);
    }

    public List<Type> findAllRevenueTypes() {
        return tr.findAllRevenueTypes();
    }

    public List<Type> findAllExpenseTypes() {
        return tr.findAllExpenseTypes();
    }

    public List<Type> findAllAdjReasons() {
        return tr.findAllAdjReasons();
    }

    public Type findTypeByNameAndCategory(String typeName, String typeCategory) {
        return tr.findTypeByNameAndCategory(typeName, typeCategory);
    }

    public List<Type> findTypesByIdsAndCategoryIn(String[] ids, String typeCategory) {
        List<String> s = Arrays.asList(ids);
        List<Long> formattedIds = new ArrayList<>();
        for (String id : s) {
            formattedIds.add(Long.parseLong(id));
        }

        return tr.findTypesByIdsAndCategoryIn(formattedIds, typeCategory);
    }

    public List<TimePeriod> findTimePeriodsByIds(String[] ids) {
        List<String> s = Arrays.asList(ids);
        List<Long> formattedIds = new ArrayList<>();
        for (String id : s) {
            formattedIds.add(Long.parseLong(id));
        }

        return tpr.findTimePeriodsByIdsIn(formattedIds);
    }

    public List<Expense> findAllExpense() {
        return er.findAll();
    }

    public Expense updateExpense(Expense expense) {
        return er.save(expense);
    }

    public List<Revenue> findAllRevenue() {
        return revr.findAll();
    }

    public Revenue updateRevenue(Revenue revenue) {
        return revr.save(revenue);
    }

    public List<TimePeriod> findTimePeriodsByDates(Date fromDate, Date toDate) {
        return tpr.findTimePeriodsByDates(fromDate, toDate);
    }

    public DataModel updateData(DataModel data) {
        return dr.save(data);
    }

    public DataRowModel updateDataRow(DataRowModel dr) {
        return drr.save(dr);
    }

    public DataModel findDataModelByReportId(Report report) {
        return dr.findDataByReportId(report);
    }

    public List<DataRowModel> findDataRowModelsByDataModel(DataModel data) {
        return drr.findDataRowModelsByData(data);
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getRevenueByCustomerAndPeriodIn(List<Customer> customers, List<TimePeriod> timePeriods) {
        List<JSONObject> rows = new ArrayList<>();

        List<Revenue> revenues = new ArrayList<>();
        revenues = revr.getRevenueByCustomersAndTimePeriodsIn(customers, timePeriods);

        Map<Customer, Map<Type, Map<TimePeriod, List<Revenue>>>> map = revenues.stream()
                .collect(Collectors.groupingBy(Revenue::getCustomer,
                            Collectors.groupingBy(Revenue::getType, Collectors.groupingBy(Revenue::getTimePeriod))));

        for (Map.Entry<Customer, Map<Type, Map<TimePeriod, List<Revenue>>>> e : map.entrySet()) {
            Map<Type, Map<TimePeriod, List<Revenue>>> byType = e.getValue();
            Customer c = e.getKey();

            for (Map.Entry<Type, Map<TimePeriod, List<Revenue>>> bt : byType.entrySet()) {
                Type t = bt.getKey();

                for (Map.Entry<TimePeriod, List<Revenue>> tp : bt.getValue().entrySet()) {
                    TimePeriod timePeriod = tp.getKey();
                    List<Revenue> rs = tp.getValue();

                    BigDecimal totalRevenue = new BigDecimal(rs.stream().mapToDouble(Revenue::getTotalRevenue).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjAmount = new BigDecimal(rs.stream().mapToDouble(Revenue::getAdjustmentAmount).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

                    JSONObject js = new JSONObject();
                    js.put("Customer", c.getName());
                    js.put("Revenue Type", t.getTypeName());
                    js.put("Time Period", timePeriod.getDisplayPeriod());
                    js.put("Total Revenue", String.format("$%,.2f", totalRevenue));
                    js.put("Adjustment Amount", String.format("$%,.2f", adjAmount));

                    rows.add(js);
                }
            }
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getRevenueByTypeAndPeriodIn(List<Type> revenueTypes, List<TimePeriod> timePeriods) {
        List<JSONObject> rows = new ArrayList<>();
        List<Revenue> revenues = new ArrayList<>();
        revenues = revr.getRevenueByTypeAndPeriodIn(revenueTypes, timePeriods);

        Map<TimePeriod, Map<Type, List<Revenue>>> map = revenues.stream()
                .collect(Collectors.groupingBy(Revenue::getTimePeriod, Collectors.groupingBy(Revenue::getType)));

        for (Map.Entry<TimePeriod, Map<Type, List<Revenue>>> e : map.entrySet()) {
            Map<Type, List<Revenue>> byType = e.getValue();
            TimePeriod tp = e.getKey();

            for (Map.Entry<Type, List<Revenue>> bt : byType.entrySet()) {
                Type t = bt.getKey();
                List<Revenue> rs = bt.getValue();

                BigDecimal totalRevenue = new BigDecimal(rs.stream().mapToDouble(Revenue::getTotalRevenue).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal adjAmount = new BigDecimal(rs.stream().mapToDouble(Revenue::getAdjustmentAmount).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

                JSONObject js = new JSONObject();
                js.put("Time Period", tp.getDisplayPeriod());
                js.put("Revenue Type", t.getTypeName());
                js.put("Total Revenue", String.format("$%,.2f", totalRevenue));
                js.put("Adjustment Amount", String.format("$%,.2f", adjAmount));

                rows.add(js);
            }
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getRevenueByTimePeriodsIn(List<TimePeriod> timePeriods) {
        List<JSONObject> rows = new ArrayList<>();
        List<Revenue> revenues = new ArrayList<>();
        revenues = revr.getRevenueByTimePeriodsIn(timePeriods);

        Map<TimePeriod, Map<Type, List<Revenue>>> map = revenues.stream()
                .collect(Collectors.groupingBy(Revenue::getTimePeriod, Collectors.groupingBy(Revenue::getType)));

        for (Map.Entry<TimePeriod, Map<Type, List<Revenue>>> e : map.entrySet()) {
            Map<Type, List<Revenue>> byType = e.getValue();
            TimePeriod tp = e.getKey();

            for (Map.Entry<Type, List<Revenue>> bt : byType.entrySet()) {
                Type t = bt.getKey();
                List<Revenue> rs = bt.getValue();

                BigDecimal totalRevenue = new BigDecimal(rs.stream().mapToDouble(Revenue::getTotalRevenue).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal adjAmount = new BigDecimal(rs.stream().mapToDouble(Revenue::getAdjustmentAmount).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

                JSONObject js = new JSONObject();
                js.put("Time Period", tp.getDisplayPeriod());
                js.put("Revenue Type", t.getTypeName());
                js.put("Total Revenue", String.format("$%,.2f", totalRevenue));
                js.put("Adjustment Amount", String.format("$%,.2f", adjAmount));

                rows.add(js);
            }
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getExpenseByTypeAndPeriodIn(List<Type> expenseTypes, List<TimePeriod> timePeriods) {
        List<JSONObject> rows = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        expenses = er.getExpenseByTypeAndPeriodIn(expenseTypes, timePeriods);

        Map<TimePeriod, Map<Type, List<Expense>>> map = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getTimePeriod, Collectors.groupingBy(Expense::getType)));

        for (Map.Entry<TimePeriod, Map<Type, List<Expense>>> e : map.entrySet()) {
            Map<Type, List<Expense>> byType = e.getValue();
            TimePeriod tp = e.getKey();

            for (Map.Entry<Type, List<Expense>> bt : byType.entrySet()) {
                Type t = bt.getKey();
                List<Expense> rs = bt.getValue();

                BigDecimal totalExpense = new BigDecimal(rs.stream().mapToDouble(Expense::getTotalCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxCost = new BigDecimal(rs.stream().mapToDouble(Expense::getTaxCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal expenseCost = new BigDecimal(rs.stream().mapToDouble(Expense::getExpenseCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

                JSONObject js = new JSONObject();
                js.put("Time Period", tp.getDisplayPeriod());
                js.put("Expense Type", t.getTypeName());
                js.put("Expense Cost", String.format("$%,.2f", expenseCost));
                js.put("Tax Cost", String.format("$%,.2f", taxCost));
                js.put("Total Cost", String.format("$%,.2f", totalExpense));

                rows.add(js);
            }
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getExpenseByTimePeriodsIn(List<TimePeriod> timePeriods) {
        List<JSONObject> rows = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        expenses = er.getExpenseByTimePeriodsIn(timePeriods);

        Map<TimePeriod, Map<Type, List<Expense>>> map = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getTimePeriod, Collectors.groupingBy(Expense::getType)));

        for (Map.Entry<TimePeriod, Map<Type, List<Expense>>> e : map.entrySet()) {
            Map<Type, List<Expense>> byType = e.getValue();
            TimePeriod tp = e.getKey();

            for (Map.Entry<Type, List<Expense>> bt : byType.entrySet()) {
                Type t = bt.getKey();
                List<Expense> rs = bt.getValue();

                BigDecimal totalExpense = new BigDecimal(rs.stream().mapToDouble(Expense::getTotalCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxCost = new BigDecimal(rs.stream().mapToDouble(Expense::getTaxCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal expenseCost = new BigDecimal(rs.stream().mapToDouble(Expense::getExpenseCost).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

                JSONObject js = new JSONObject();
                js.put("Time Period", tp.getDisplayPeriod());
                js.put("Expense Type", t.getTypeName());
                js.put("Expense Cost", String.format("$%,.2f", expenseCost));
                js.put("Tax Cost", String.format("$%,.2f", taxCost));
                js.put("Total Cost", String.format("$%,.2f", totalExpense));

                rows.add(js);
            }
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public List<JSONObject> getEmployeePayrollByPayDate(Date fromDate, Date toDate) {
        List<JSONObject> rows = new ArrayList<>();
        List<EmployeePayroll> payroll = new ArrayList<>();

        payroll = empr.getEmployeePayrollByPayDate(fromDate, toDate);

        Map<Date, List<EmployeePayroll>> map = payroll.stream()
                .collect(Collectors.groupingBy(EmployeePayroll::getPayDate));

        for (Map.Entry<Date, List<EmployeePayroll>> e : map.entrySet()) {
            Date d = e.getKey();
            List<EmployeePayroll> p = e.getValue();

            TimePeriod t = tpr.findTimePeriodDateBetween(d);

            BigDecimal regularPay = new BigDecimal(p.stream().mapToDouble(EmployeePayroll::getRegularPay).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal overtimePay = new BigDecimal(p.stream().mapToDouble(EmployeePayroll::getOvertimePay).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal totalPay = new BigDecimal(p.stream().mapToDouble(EmployeePayroll::getTotalPay).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal hoursWorked = new BigDecimal(p.stream().mapToDouble(EmployeePayroll::getHoursWorked).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);

            JSONObject js = new JSONObject();
            js.put("Time Period", t.getDisplayPeriod());
            js.put("Pay Date", d.toString());
            js.put("Hours Worked", String.format("%,.2f", hoursWorked));
            js.put("Regular Pay", String.format("$%,.2f", regularPay));
            js.put("Overtime Pay", String.format("$%,.2f", overtimePay));
            js.put("Total Pay", String.format("$%,.2f", totalPay));

            rows.add(js);

        }
        return rows;
    }

}
