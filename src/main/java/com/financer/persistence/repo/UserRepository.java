/*
 * Adriel Swisher
 * CST 452
 * 
 * User Repository. Extends JPA repository for handing CRUD operations for User model
 */
package com.financer.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financer.persistence.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserId(Long userId);

    User findByUsername(String username);
}
