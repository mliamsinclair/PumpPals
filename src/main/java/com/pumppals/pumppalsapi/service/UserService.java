package com.pumppals.pumppalsapi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public boolean updateUser(UserInfo user) {
        Optional<UserInfo> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            UserInfo dbUser = existingUser.get();
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());
            dbUser.setDateOfBirth(user.getDateOfBirth());
            dbUser.setGender(user.getGender());
            dbUser.setWeight(user.getWeight());
            dbUser.setHeight(user.getHeight());
            dbUser.setFitnessGoals(user.getFitnessGoals());
            userRepository.save(dbUser);
            return true;
        }
        return false;
    }

    public boolean followUser (String username, String followUsername) {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        Optional<UserInfo> followUser = userRepository.findByUsername(followUsername);
        if (user.isPresent() && followUser.isPresent()) {
            user.get().getFollowing().add(followUsername);
            followUser.get().getFollowers().add(username);
            userRepository.save(user.get());
            userRepository.save(followUser.get());
            System.out.println(user.get().getFollowing());
            return true;
        }
        return false;
    }

    public boolean unfollowUser (String username, String unfollowUsername) {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        Optional<UserInfo> unfollowUser = userRepository.findByUsername(unfollowUsername);
        if (user.isPresent() && unfollowUser.isPresent()) {
            user.get().getFollowing().remove(unfollowUsername);
            unfollowUser.get().getFollowers().remove(username);
            userRepository.save(user.get());
            userRepository.save(unfollowUser.get());
            System.out.println(user.get().getFollowing());
            return true;
        }
        return false;
    }

    public boolean checkFollow (String username, String checkUsername) {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        Optional<UserInfo> checkUser = userRepository.findByUsername(checkUsername);
        if (user.isPresent() && checkUser.isPresent()) {
            return user.get().getFollowing().contains(checkUsername);
        }
        return false;
    }

    // return a list of users that followers follow but user does not follow
    // as well as a list of users that follow user but user does not follow
    // as well as a list of users that the users followers follow but the user does not follow
    public List<UserInfo> getRecommendedUsers(String username) {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<UserInfo> users = userRepository.findAll();
            users.remove(user.get());
            users.removeAll(user.get().getFollowing());
            return users;
        }
        return null;
    }
}
