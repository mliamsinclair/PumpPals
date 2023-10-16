package com.pumppals.pumppalsapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
