package com.pumppals.pumppalsapi.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public List<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserInfo> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserInfo> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public UserInfo createUser(UserInfo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
