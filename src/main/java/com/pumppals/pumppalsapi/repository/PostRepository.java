package com.pumppals.pumppalsapi.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pumppals.pumppalsapi.model.PostInfo;

@Repository
public interface PostRepository extends MongoRepository<PostInfo, ObjectId> {
    List<PostInfo> findByUsername(String username);
    Optional<PostInfo> findByPostId(String postId);
}
