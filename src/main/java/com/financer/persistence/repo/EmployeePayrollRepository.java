/*
 * Adriel Swisher
 * CST 452
 * 
 * EmployeePayroll Repository. Extends JPA repository for handing CRUD operations for EmployeePayroll model
 */
package com.financer.persistence.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financer.persistence.model.EmployeePayroll;

public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {
 
    @Query("SELECT e FROM EmployeePayroll e WHERE payDate >= :fromDate AND payDate <= :toDate")
    List<EmployeePayroll> getEmployeePayrollByPayDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
