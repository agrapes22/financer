package com.financer.persistence.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long timePeriodId;

    private String displayPeriod;
    private Date fromDate;
    private Date toDate;

    public TimePeriod() {}
    
    public long getTimePeriodId() {
        return timePeriodId;
    }
    public void setTimePeriodId(long timePeriodId) {
        this.timePeriodId = timePeriodId;
    }
    public String getDisplayPeriod() {
        return displayPeriod;
    }
    public void setDisplayPeriod(String displayPeriod) {
        this.displayPeriod = displayPeriod;
    }
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    
}
