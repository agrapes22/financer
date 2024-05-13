package com.financer.persistence.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.Expense;
import com.financer.persistence.model.Revenue;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;
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

    public List<Type> findAllTypes() {
        return tr.findAll();
    }

    public Type findTypeById(Long typeId) {
        return tr.findByTypeId(typeId);
    }

    public Type updateType(Type t) {
        return tr.save(t);
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

}
