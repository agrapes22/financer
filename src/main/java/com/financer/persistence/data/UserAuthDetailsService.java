/*
 * Adriel Swisher
 * CST 452
 * 
 * User auth service for customer UserDetailsService related to user authentication
 */
package com.financer.persistence.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.User;
import com.financer.persistence.repo.UserRepository;

@Service
public class UserAuthDetailsService implements UserDetailsService  {

    @Autowired
    UserRepository ur;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = ur.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public User save(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPasswordHash(encoder.encode(user.getPassword()));
        return ur.save(newUser);
    }

    public boolean checkIfOldPasswordValid(User u, String oldPassword) {
        User user = ur.findByUserId(u.getUserId());
        boolean check = encoder.matches(oldPassword, user.getPassword());
        return check;
    }

    public void updatePassword(User u, String newPassword) {
        ur.updatePassword(encoder.encode(newPassword), u.getUserId());
    }

}
