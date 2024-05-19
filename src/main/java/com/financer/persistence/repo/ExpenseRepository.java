/*
 * Adriel Swisher
 * CST 452
 * 
 * Expense Repository. Extends JPA repository for handing CRUD operations for Expense model
 */
package com.financer.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Expense;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    @Query("SELECT e FROM Expense e WHERE type IN :expenseTypes AND timePeriod IN :timePeriods")
    List<Expense> getExpenseByTypeAndPeriodIn(@Param("expenseTypes") List<Type> expenseTypes, @Param("timePeriods") List<TimePeriod> timePeriods);

    @Query("SELECT e FROM Expense e WHERE timePeriod IN :timePeriods")
    List<Expense> getExpenseByTimePeriodsIn(@Param("timePeriods") List<TimePeriod> timePeriods);

}
