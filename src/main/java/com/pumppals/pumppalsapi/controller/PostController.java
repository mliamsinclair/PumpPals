package com.pumppals.pumppalsapi.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pumppals.pumppalsapi.exceptions.FileDownloadException;
import com.pumppals.pumppalsapi.model.PostInfo;
import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.service.FileService;
import com.pumppals.pumppalsapi.service.PostService;
import com.pumppals.pumppalsapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

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
        post.setHasPicture(false);
        post = postService.createPost(post);
        System.out.println(post);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/posts/create/picture")
    public ResponseEntity<String> createPicturePost(@RequestParam("file") MultipartFile file, Principal principal,
            @RequestPart("post") PostInfo post) {
        System.out.println("createPicturePost");
        System.out.println("createPicturePost for " + principal.getName() + " with file " + file.getOriginalFilename());
        String username = principal.getName();
        post.setUsername(username);
        Optional<UserInfo> user = userService.getUserByUsername(username);
        post.setName(user.get().getName());
        post.setUploadDate(LocalDateTime.now());
        post.setHasPicture(true);
        System.out.println(post);
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        System.out.println("Uploading picture for " + username + " with extension " + fileExtension);
        if (fileExtension != null
                && (fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("gif"))) {
            try {
                // set file name to postId + .png
                String filename = file.getOriginalFilename();
                post.setPictureName(filename);
                System.out.println(filename);
                // upload original image to s3
                fileService.uploadFile(file, filename);
                // create post
                post = postService.createPost(post);
                System.out.println(post);

                return ResponseEntity.ok().body("Profile picture uploaded successfully.");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload picture.");
            }
        } else {
            return ResponseEntity.badRequest()
                    .body("Invalid file format. Only PNG, JPG, and JPEG formats are allowed.");
        }
    }

    // returns a post's picture
    @GetMapping("/posts/picture/{id}")
    public ResponseEntity<Object> getPostPicture(@PathVariable String id) {
        try {
            System.out.println("getPostPicture for " + id);
            PostInfo post = postService.getPostByPostId(id);
            String filename = post.getPictureName();
            System.out.println("getPostPicture for " + id + " with filename " + filename);
            ResponseEntity<Object> response = ResponseEntity.ok().body(fileService.downloadFile(filename));
            // Delete the picture from local storage after sending
            return response;
        } catch (FileDownloadException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download picture.");
        }
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
