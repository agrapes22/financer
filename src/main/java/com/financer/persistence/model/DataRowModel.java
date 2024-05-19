/*
 * Adriel Swisher
 * CST 452
 * 
 * DataRow model to hold JSON records by line for report model. Related to data and report models
 */
package com.financer.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="data_row")
public class DataRowModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dataRowId;

    @ManyToOne
    @JoinColumn(name="dataId")
    private DataModel data;

    private String dataDetail;

    public long getDataRowId() {
        return dataRowId;
    }

    public void setDataRowId(long dataRowId) {
        this.dataRowId = dataRowId;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public String getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(String dataDetail) {
        this.dataDetail = dataDetail;
    }

}
