package com.financer.persistence.repo;

import java.util.List;

//import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Override
    @Query("SELECT c FROM Customer c")
    List<Customer> findAll();
    
    Customer findByCustomerId(Long customerId);

    @Query("SELECT c FROM Customer c WHERE customerId IN :ids")
    List<Customer> findCustomersByIdsIn(@Param("ids") List<Long> ids);
}
