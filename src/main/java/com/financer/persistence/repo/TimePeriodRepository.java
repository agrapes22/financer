/*
 * Adriel Swisher
 * CST 452
 * 
 * Time Period Repository. Extends JPA repository for handing CRUD operations for TimePeriod model
 */
package com.financer.persistence.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financer.persistence.model.TimePeriod;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
    TimePeriod findByTimePeriodId(long timePeriodId);

    @Query("SELECT t FROM TimePeriod t WHERE timePeriodId IN :ids")
    List<TimePeriod> findTimePeriodsByIdsIn(@Param("ids") List<Long> ids);

    @Query("SELECT timePeriodId FROM TimePeriod WHERE fromDate >= :fromDate AND toDate <= :toDate")
    List<Long> findIdsByDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT t FROM TimePeriod t WHERE fromDate >= :fromDate AND toDate <= :toDate")
    List<TimePeriod> findTimePeriodsByDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT t FROM TimePeriod t WHERE :date >= fromDate AND :date <= toDate")
    TimePeriod findTimePeriodDateBetween(@Param("date") Date date);
}
