/*
 * Adriel Swisher
 * CST 452
 * 
 * Customer data service for database actions related to customer model
 */
package com.financer.persistence.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.Customer;
import com.financer.persistence.repo.CustomerRepository;
import com.financer.persistence.repo.RevenueRepository;

@Service
public class CustomerDataService {

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    RevenueRepository rr;

    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    public Customer findById(long id) {
        return customerRepo.findByCustomerId(id);
    }

    public Customer create(Customer t) {
        return customerRepo.save(t);
    }

    public void delete(Customer t) throws DataIntegrityViolationException {
        customerRepo.delete(t);
    }

    public Customer update(Customer t) {
        return customerRepo.save(t);
    }

    public List<Customer> findCustomersByIdsIn(String[] ids) {
        List<String> s = Arrays.asList(ids);
        List<Long> formattedIds = new ArrayList<>();
        for (String id : s) {
            formattedIds.add(Long.parseLong(id));
        }

        return customerRepo.findCustomersByIdsIn(formattedIds);
    }

    public List<Customer> findCustomersBySearchTerm(String search) {
        return customerRepo.findByNameContainsIgnoreCaseOrContactNameContainsIgnoreCase(search, search);
    }

    // public List<Revenue> findCustomerRevenues(Customer customer) {

    // }

}
