/*
 * Adriel Swisher
 * CST 452
 * 
 * Revenue model. Stores revenue data entered by users.
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
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long revenueId;

    @ManyToOne
    @JoinColumn(name="timePeriodId")
    private TimePeriod timePeriod;

    @ManyToOne
    @JoinColumn(name="typeId")
    private Type type;

    @ManyToOne
    @JoinColumn(name="customerId")
    private Customer customer;

    private Date dateReceived;
    private double totalRevenue = 0.0;
    private double adjustmentAmount = 0.0;

    @ManyToOne
    @JoinColumn(name="adjustmentTypeId")
    private Type adjustmentReason;

    public long getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(long revenueId) {
        this.revenueId = revenueId;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public Type getAdjustmentReason() {
        return adjustmentReason;
    }

    public void setAdjustmentReason(Type adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    

}