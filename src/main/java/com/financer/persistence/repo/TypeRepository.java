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
}
