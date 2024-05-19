/*
 * Adriel Swisher
 * CST 452
 * 
 * EmployeePayroll model. Data imported from external source and is read only for reports.
 */
package com.financer.persistence.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmployeePayroll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long employeePayrollId;

    private long employeeId;
    private Date payDate;
    private double hoursWorked;
    private double totalPay;
    private double regularPay;
    private double overtimePay;

    public long getEmployeePayrollId() {
        return employeePayrollId;
    }
    public void setEmployeePayrollId(long employeePayrollId) {
        this.employeePayrollId = employeePayrollId;
    }
    public long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date paydate) {
        this.payDate = paydate;
    }
    public double getHoursWorked() {
        return hoursWorked;
    }
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    public double getTotalPay() {
        return totalPay;
    }
    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
    public double getRegularPay() {
        return regularPay;
    }
    public void setRegularPay(double regularPay) {
        this.regularPay = regularPay;
    }
    public double getOvertimePay() {
        return overtimePay;
    }
    public void setOvertimePay(double overtimePay) {
        this.overtimePay = overtimePay;
    }

}
