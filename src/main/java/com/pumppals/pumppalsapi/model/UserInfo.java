package com.pumppals.pumppalsapi.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// mongoDB document to store user info
@Document(collection = "user")
// model to handle user info
public class UserInfo {
    // user info fields
    @Id
    private ObjectId id;
    private List<String> fitnessGoals;
    private String name;
    private String username;
    private String password;
    // roles to check if user has permission to access certain endpoints
    // all users have ROLE_USER
    private String roles = "ROLE_USER";
    private int age;
    private int height;
    private int weight;
    private String bio;
}
