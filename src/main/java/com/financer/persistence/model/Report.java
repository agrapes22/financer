/*
 * Adriel Swisher
 * CST 452
 * 
 * Report model. Used to compile data for users to view and export
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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;

    private String name;

    private Date createdDate;
    private String customerIds;
    private String types;

    @ManyToOne
    @JoinColumn(name="timePeriodFromId")
    private TimePeriod fromPeriod;

    @ManyToOne
    @JoinColumn(name="timePeriodToId")
    private TimePeriod toPeriod;

    @ManyToOne
    @JoinColumn(name="typeId")
    private Type reportType;

    public Report() {}

    public String toString() {
        return reportId + " " + name + " " + reportType.getTypeName() + " " + createdDate + " " + fromPeriod.getFromDate() + " " + toPeriod.getToDate() + " " +  customerIds;
    }

    public long getReportId() {
        return reportId;
    }
    public void setReportId(long reportId) {
        this.reportId = reportId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Type getReportType() {
        return reportType;
    }
    public void setReportType(Type reportType) {
        this.reportType = reportType;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public long getFromPeriodId() {
        return fromPeriod.getTimePeriodId();
    }
    public TimePeriod getFromPeriod() {
        return fromPeriod;
    }
    public void setFromPeriod(TimePeriod fromPeriod) {
        this.fromPeriod = fromPeriod;
    }
    public long getToPeriodId() {
        return toPeriod.getTimePeriodId();
    }
    public TimePeriod getToPeriod() {
        return toPeriod;
    }
    public void setToPeriod(TimePeriod toPeriod) {
        this.toPeriod = toPeriod;
    }
    public String getCustomerIds() {
        return customerIds;
    }
    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }
    public String getTypes() {
        return this.types;
    }
    public void setTypes(String types) {
        this.types = types;
    }

}
