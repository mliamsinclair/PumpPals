package com.pumppals.pumppalsapi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pumppals.pumppalsapi.model.PPUser;

@Repository
public interface UserRepo extends MongoRepository<PPUser, ObjectId> {
    Optional<PPUser> findByUsername(String username);
}
