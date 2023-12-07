package com.pumppals.pumppalsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pumppals.pumppalsapi.model.PostInfo;
import com.pumppals.pumppalsapi.repository.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public List<PostInfo> getAllPosts() {
        List<PostInfo> posts = postRepository.findAll();
        // sort by upload date
        posts.sort((p1, p2) -> p2.getUploadDate().compareTo(p1.getUploadDate()));
        return posts;
    }

    public Optional<PostInfo> getPostById(ObjectId id) {
        return postRepository.findById(id);
    }

    public List<PostInfo> getPostsByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    public PostInfo createPost(PostInfo post) {
        post = postRepository.save(post);
        post.setPostId(post.getId().toString());
        return postRepository.save(post);
    }

    public PostInfo updatePost(PostInfo post) {
        return postRepository.save(post);
    }

    public void deletePost(ObjectId id) {
        postRepository.deleteById(id);
    }

    public void deletePostsByUsername(String username) {
        List<PostInfo> posts = postRepository.findByUsername(username);
        for (PostInfo post : posts) {
            postRepository.deleteById(post.getId());
        }
    }

    public void likePost(ObjectId id, String username) {
        PostInfo post = postRepository.findById(id).get();
        post.setLikes(post.getLikers().size() + 1);
        // add username to likers
        post.getLikers().add(username);
        System.out.println("likePost: " + post.getLikers() + " " + username + " num likes: " + post.getLikes());
        postRepository.save(post);
    }

    public void unlikePost(ObjectId id, String username) {
        PostInfo post = postRepository.findById(id).get();
        post.setLikes(post.getLikers().size() - 1);
        // remove username from likers
        post.getLikers().remove(username);
        System.out.println("unlikePost: " + post.getLikers() + " " + username + " num likes: " + post.getLikes());
        postRepository.save(post);
    }

    public void commentPost(ObjectId id, String username, String comment) {
        PostInfo post = postRepository.findById(id).get();
        post.getComments().add(comment);
        post.getCommenters().add(username);
        postRepository.save(post);
    }

    public void uncommentPost(ObjectId id, String username, String comment) {
        PostInfo post = postRepository.findById(id).get();
        post.getComments().remove(comment);
        post.getCommenters().remove(username);
        postRepository.save(post);
    }

    public List<PostInfo> getFollowingPosts(String username) {
        List<String> following = userService.getUserByUsername(username).get().getFollowing();
        List<PostInfo> posts = new ArrayList<>();
        for (String follow : following) {
            List<PostInfo> tempPosts = postRepository.findByUsername(follow);
            for (PostInfo post : tempPosts) {
                posts.add(post);
            }
        }
        // add user's posts
        List<PostInfo> userPosts = postRepository.findByUsername(username);
        for (PostInfo post : userPosts) {
            posts.add(post);
        }
        // sort by upload date
        posts.sort((p1, p2) -> p2.getUploadDate().compareTo(p1.getUploadDate()));
        return posts;
    }

    public PostInfo getPostByPostId(String postId) {
        return postRepository.findByPostId(postId).orElse(null);
    }
}
