package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
}
