/*
 * Adriel Swisher
 * CST 452
 * 
 * Expense model. Stores expenses entered by users. Related to reports model.
 */
package com.financer.persistence.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;

    @ManyToOne
    @JoinColumn(name="timePeriodId")
    private TimePeriod timePeriod;

    @ManyToOne
    @JoinColumn(name="typeId")
    private Type type;

    private Date datePaid;
    private double totalCost = 0.0;
    private double taxCost = 0.0;
    private double expenseCost = 0.0;

    public long getexpenseId() {
        return expenseId;
    }
    public void setexpenseId(long expenseId) {
        this.expenseId = expenseId;
    }
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public Date getDatePaid() {
        return datePaid;
    }
    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }
    public double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    public double getTaxCost() {
        return taxCost;
    }
    public void setTaxCost(double taxCost) {
        this.taxCost = taxCost;
    }
    public double getExpenseCost() {
        return expenseCost;
    }
    public void setExpenseCost(double expenseCost) {
        this.expenseCost = expenseCost;
    }

}
