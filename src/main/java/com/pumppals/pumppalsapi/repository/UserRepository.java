package com.pumppals.pumppalsapi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pumppals.pumppalsapi.model.UserInfo;

@Repository
public interface UserRepository extends MongoRepository<UserInfo, ObjectId> {
    Optional<UserInfo> findByUsername(String username);
}
