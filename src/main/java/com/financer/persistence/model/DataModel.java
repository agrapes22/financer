/*
 * Adriel Swisher
 * CST 452
 * 
 * Data model for data record related to report record. Contains column definitions for report. Related to report and datarow models
 */
package com.financer.persistence.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="data")
public class DataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataId;

    @ManyToOne
    @JoinColumn(name="reportId")
    private Report report;

    private String columnDefinition;
    private Date createdDate;

    public long getDataId() {
        return dataId;
    }
    public void setDataId(long dataId) {
        this.dataId = dataId;
    }
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }
    public String getColumnDefinition() {
        return columnDefinition;
    }
    public void setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}