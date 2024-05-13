package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByReportId(Long reportId);
}
