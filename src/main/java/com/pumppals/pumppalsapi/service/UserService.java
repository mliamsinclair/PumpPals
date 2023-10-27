package com.pumppals.pumppalsapi.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pumppals.pumppalsapi.model.PPUser;
import com.pumppals.pumppalsapi.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepository;

    public List<PPUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<PPUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<PPUser> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public PPUser createUser(PPUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
