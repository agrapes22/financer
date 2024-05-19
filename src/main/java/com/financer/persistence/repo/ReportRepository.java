/*
 * Adriel Swisher
 * CST 452
 * 
 * Report Repository. Extends JPA repository for handing CRUD operations for Report model
 */
package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByReportId(Long reportId);
}
