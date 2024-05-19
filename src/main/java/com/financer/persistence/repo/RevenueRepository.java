/*
 * Adriel Swisher
 * CST 452
 * 
 * Revenue Repository. Extends JPA repository for handing CRUD operations for Revenue model
 */
package com.financer.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Customer;
import com.financer.persistence.model.Revenue;
import com.financer.persistence.model.TimePeriod;
import com.financer.persistence.model.Type;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    @Query("SELECT c.name, tp.displayPeriod, t.typeName, SUM(r.totalRevenue) AS total_cost, SUM(r.adjustmentAmount) AS adj_amount FROM Revenue r, TimePeriod tp, types t, Customer c WHERE r.customer IN :customerIds AND r.timePeriod IN :timePeriodIds AND r.timePeriod = tp.timePeriodId AND r.type = t.typeId AND r.customer = c.customerId  GROUP BY c.name, tp.displayPeriod, t.typeName")
    Revenue getRevenueByCustomerAndPeriodIn(@Param("customerIds") List<Long> customerIds, @Param("timePeriodIds") List<Long> timePeriodIds);

    @Query("SELECT r FROM Revenue r where r.customer IN :customers AND r.timePeriod IN :timePeriods")
    List<Revenue> getRevenueByCustomersAndTimePeriodsIn(@Param("customers") List<Customer> customers, @Param("timePeriods") List<TimePeriod> timePeriods);

    @Query("SELECT r FROM Revenue r WHERE type IN :revenueTypes AND timePeriod IN :timePeriods")
    List<Revenue> getRevenueByTypeAndPeriodIn(@Param("revenueTypes") List<Type> revenueTypes, @Param("timePeriods") List<TimePeriod> timePeriods);

    @Query("SELECT r FROM Revenue r WHERE timePeriod IN :timePeriods")
    List<Revenue> getRevenueByTimePeriodsIn(@Param("timePeriods") List<TimePeriod> timePeriods);
}