package com.pumppals.pumppalsapi.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
public class PostInfo {
    @Id
    private ObjectId id;
    private String username;
    private String name;
    private String title;
    private String content;
    private LocalDateTime uploadDate; 
    private int likes = 0;
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<String> commenters = new ArrayList<String>();
    private ArrayList<String> likers = new ArrayList<String>();
    private String postId;
    private boolean hasPicture;
    private String pictureName;

    public boolean getHasPicture() {
        return this.hasPicture;
    }
    
    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }
}
