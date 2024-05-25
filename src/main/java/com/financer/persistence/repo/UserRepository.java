/*
 * Adriel Swisher
 * CST 452
 * 
 * User Repository. Extends JPA repository for handing CRUD operations for User model
 */
package com.financer.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.financer.persistence.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserId(Long userId);

    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password_hash = :newPassword WHERE user_id = :userId", nativeQuery = true)
    void updatePassword(@Param("newPassword") String newPassword, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET name = :name WHERE user_id = :userId", nativeQuery = true)
    int updateName(@Param("name") String name, @Param("userId") Long userId);

    List<User> findByNameContainsIgnoreCaseOrUsernameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAccountTypeEqualsIgnoreCase(String name, String username, String email, String accountType);

    @Query("SELECT u FROM users u WHERE accountType = 'R'")
    List<User> findRegularUsers();

    @Query("SELECT u FROM users u WHERE accountType = 'A'")
    List<User> findAdminUsers();
}
