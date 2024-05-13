package com.financer.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financer.persistence.model.TimePeriod;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
    TimePeriod findByTimePeriodId(long timePeriodId);

    @Query("SELECT t FROM TimePeriod t WHERE timePeriodId IN :ids")
    List<TimePeriod> findTimePeriodsByIdsIn(@Param("ids") List<Long> ids);
}
