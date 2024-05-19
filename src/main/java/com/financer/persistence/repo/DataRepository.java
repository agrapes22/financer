/*
 * Adriel Swisher
 * CST 452
 * 
 * Data Repository. Extends JPA repository for handing CRUD operations for Data model
 */
package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.DataModel;
import com.financer.persistence.model.Report;


@Repository
public interface DataRepository extends JpaRepository<DataModel, Long> {

    @Query("SELECT d FROM data d WHERE report = :report")
    DataModel findDataByReportId(Report report);

}
