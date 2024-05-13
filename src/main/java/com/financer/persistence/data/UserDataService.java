package com.financer.persistence.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financer.persistence.model.User;
import com.financer.persistence.repo.UserRepository;

@Service
public class UserDataService {

    @Autowired
    UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(long id) {
        return userRepo.findByUserId(id);
    }

    public User create(User t) {
        return userRepo.save(t);
    }

    public void delete(User t) {
        userRepo.delete(t);
    }

    public User update(User t) {
        return userRepo.save(t);
    }
    

}
