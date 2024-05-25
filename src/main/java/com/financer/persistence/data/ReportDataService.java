/*
 * Adriel Swisher
 * CST 452
 * 
 * Report data service for database actions related to report model
 */
package com.financer.persistence.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.Report;
import com.financer.persistence.repo.ReportRepository;

@Service
public class ReportDataService {

    @Autowired
    ReportRepository rr;

    public List<Report> findAll() {
        return rr.findAll();
    }

    public Report update(Report r) {
        return rr.save(r);
    }

    public Report findByReportId(Long reportId) {
        return rr.findByReportId(reportId);
    }

    public void delete(Report report) {
        rr.delete(report);
    }

}
