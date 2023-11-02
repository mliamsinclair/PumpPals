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
@Document(collection = "user")
public class UserInfo {
    @Id
    private ObjectId id;
    private List<String> fitnessGoals;
    private String name;
    private String username;
    private String password;
    private String roles = "ROLE_USER";
    private int age;
    private int height;
    private int weight;
    private String bio;
}
