package com.financer.persistence.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import javax.persistence.EntityManager;
// import javax.persistence.EntityManagerFactory;
// import javax.persistence.Persistence;
// import javax.persistence.TypedQuery;
// import javax.persistence.criteria.CriteriaBuilder;
// import javax.persistence.criteria.CriteriaQuery;
// import javax.persistence.criteria.Root;

import com.financer.persistence.model.Customer;
import com.financer.persistence.repo.CustomerRepository;

@Service
public class CustomerDataService implements DataAccessInterface<Customer> {

    @Autowired
    CustomerRepository customerRepo;

    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    public Customer findById(long id) {
        return customerRepo.findByCustomerId(id);
    }

    @Override
    public Customer create(Customer t) {
        return customerRepo.save(t);
    }

    @Override
    public void delete(Customer t) {
        customerRepo.delete(t);
    }

    @Override
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

    // EntityManagerFactory emf;
    // EntityManager em;

    // public CustomerDataService() {
    //     emf = Persistence.createEntityManagerFactory("Financer_JPA");
    //     em = emf.createEntityManager();
    // }

    // @Override
    // public <S extends Customer> S save(S entity) {
    //     em.getTransaction().begin();
    //     em.persist(entity);
    //     em.getTransaction().commit();
    //     //Customer updatedCust = em.find(Customer.class, entity.getId());
    //     em.refresh(entity);
    //     em.close();
    //     emf.close();

    //     return entity;
    // }

    // //@Override
    // public <S extends Customer> Iterable<S> save(Iterable<S> entities) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'save'");
    // }

    // //@Override
    // public Customer findOne(Long id) {
    //     Customer c = em.find(Customer.class, id);
    //     return c;
    // }

    // //@Override
    // public boolean exists(Long id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'exists'");
    // }

    // @Override
    // public Iterable<Customer> findAll() {
    //     CriteriaBuilder cb = em.getCriteriaBuilder();
    //     CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
    //     Root<Customer> rootEntry = cq.from(Customer.class);
    //     CriteriaQuery<Customer> all = cq.select(rootEntry);
    //     TypedQuery<Customer> allQuery = em.createQuery(all);
    //     return allQuery.getResultList();
    // }

    // //@Override
    // public Iterable<Customer> findAll(Iterable<Long> ids) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    // }

    // @Override
    // public long count() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'count'");
    // }

    // //@Override
    // public void delete(Long id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'delete'");
    // }

    // @Override
    // public void delete(Customer entity) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'delete'");
    // }

    // //@Override
    // public void delete(Iterable<? extends Customer> entities) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'delete'");
    // }

    // @Override
    // public void deleteAll() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    // }

    // @Override
    // public List<Customer> findByName(String name) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    // }

    // @Override
    // public void deleteAll(Iterable<? extends Customer> entities) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    // }

    // @Override
    // public void deleteAllById(Iterable<? extends Long> ids) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    // }

    // @Override
    // public void deleteById(Long id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    // }

    // @Override
    // public boolean existsById(Long id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    // }

    // @Override
    // public Iterable<Customer> findAllById(Iterable<Long> ids) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    // }

    // @Override
    // public Optional<Customer> findById(Long id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findById'");
    // }

    // @Override
    // public <S extends Customer> Iterable<S> saveAll(Iterable<S> entities) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    // }

}
