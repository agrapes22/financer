package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Revenue;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {

}
