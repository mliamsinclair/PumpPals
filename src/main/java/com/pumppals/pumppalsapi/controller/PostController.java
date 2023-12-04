package com.pumppals.pumppalsapi.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumppals.pumppalsapi.model.PostInfo;
import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.service.PostService;
import com.pumppals.pumppalsapi.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // returns a list of all recent posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostInfo>> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    // returns a list of all posts by a user
    @GetMapping("/posts/{username}")
    public ResponseEntity<List<PostInfo>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(postService.getPostsByUsername(username));
    }

    // creates a new post
    @PostMapping("/posts/create")
    public ResponseEntity<PostInfo> createPost(Principal principal, @RequestBody PostInfo post) {
        post.setUsername(principal.getName());
        Optional<UserInfo> user = userService.getUserByUsername(principal.getName());
        post.setName(user.get().getName());
        post.setUploadDate(LocalDateTime.now());
        post = postService.createPost(post);
        System.out.println(post);
        return ResponseEntity.ok().body(post);
    }

    // updates a post
    @PutMapping("/posts/update")
    public ResponseEntity<PostInfo> updatePost(@RequestBody PostInfo post) {
        return ResponseEntity.ok().body(postService.updatePost(post));
    }

    // deletes a post
    @DeleteMapping("/posts/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        postService.deletePost(objectId);
        return ResponseEntity.ok().build();
    }

    // returns a list of post that a user follows
    @GetMapping("/posts/following")
    public ResponseEntity<List<PostInfo>> getFollowingPosts(Principal principal) {
        return ResponseEntity.ok().body(postService.getFollowingPosts(principal.getName()));
    }

    // likes a post
    @PutMapping("/posts/like/{id}")
    public ResponseEntity<?> likePost(Principal principal, @PathVariable String id) {
        String name = principal.getName();
        System.out.println("likePost");
        ObjectId objectId = new ObjectId(id);
        postService.likePost(objectId, name);
        return ResponseEntity.ok().build();
    }

    // unlikes a post
    @PutMapping("/posts/unlike/{id}")
    public ResponseEntity<?> unlikePost(Principal principal, @PathVariable String id) {
        String name = principal.getName();
        System.out.println("unlikePost");
        ObjectId objectId = new ObjectId(id);
        postService.unlikePost(objectId, name);
        return ResponseEntity.ok().build();
    }

    // comments on a post
    @PutMapping("/posts/comment/{id}")
    public ResponseEntity<?> commentPost(@PathVariable String id, @RequestBody String comment, Principal principal) {
        ObjectId objectId = new ObjectId(id);
        postService.commentPost(objectId, principal.getName(), comment);
        return ResponseEntity.ok().build();
    }

    // uncomments on a post
    @PutMapping("/posts/uncomment/{id}")
    public ResponseEntity<?> uncommentPost(@PathVariable String id, @RequestBody String comment, Principal principal) {
        ObjectId objectId = new ObjectId(id);
        postService.uncommentPost(objectId, principal.getName(), comment);
        return ResponseEntity.ok().build();
    }
}
