/*
 * Adriel Swisher
 * CST 452
 * 
 * Type Repository. Extends JPA repository for handing CRUD operations for Type model
 */
package com.financer.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByTypeId(Long typeId);

    @Query("SELECT t FROM types t WHERE t.typeCategory = 'Revenue'")
    List<Type> findAllRevenueTypes();

    @Query("SELECT t FROM types t WHERE t.typeCategory = 'Expense'")
    List<Type> findAllExpenseTypes();

    @Query("SELECT t FROM types t WHERE t.typeCategory = 'Adjustment'")
    List<Type> findAllAdjReasons();

    @Query("SELECT t FROM types t WHERE t.typeName = :typeName AND t.typeCategory = :typeCategory")
    Type findTypeByNameAndCategory(@Param("typeName") String typeName, @Param("typeCategory") String typeCategory);

    @Query("SELECT t FROM types t WHERE t.typeName = 'Customer' AND t.typeCategory ='Report'")
    Type findCustomerReportType();

    @Query("SELECT t FROM types t WHERE t.typeName = 'Revenue' AND t.typeCategory = 'Report'")
    Type findRevenueReportType();

    @Query("SELECT t FROM types t WHERE t.typeName = 'Expense' AND t.typeCategory = 'Report'")
    Type getExpenseReportType();
    
    @Query("SELECT t FROM types t WHERE t.typeName = 'Payroll' AND t.typeCategory = 'Report'")
    Type getPayrollReportType();

    @Query("SELECT t FROM types t WHERE typeId IN :ids AND typeCategory = :categoryName")
    List<Type> findTypesByIdsAndCategoryIn(@Param("ids") List<Long> ids, @Param("categoryName") String categoryName);
}
