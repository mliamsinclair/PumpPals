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

    // password encoder to encode password
    @Autowired
    PasswordEncoder passwordEncoder;

    // user repository to retrieve user info from database
    @Autowired
    private UserRepository userRepository;

    // get all users
    public List<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by username
    public Optional<UserInfo> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // get user by id
    public Optional<UserInfo> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    // create user
    public UserInfo createUser(UserInfo user) {
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
