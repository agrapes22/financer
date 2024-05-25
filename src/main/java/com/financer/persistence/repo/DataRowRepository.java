/*Adriel Swisher
 * CST 452
 * 
 * DataRow Repository. Extends JPA repository for handing CRUD operations for DataRow model */
package com.financer.persistence.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.DataModel;
import com.financer.persistence.model.DataRowModel;
@Repository
public interface DataRowRepository extends JpaRepository<DataRowModel, Long> {

    @Query("SELECT d FROM data_row d WHERE data = :data")
    List<DataRowModel> findDataRowModelsByData(DataModel data);

    @Transactional
    @Modifying
    int deleteByData(DataModel data);
}
