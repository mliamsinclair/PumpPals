package com.pumppals.pumppalsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumppals.pumppalsapi.model.PPUser;
import com.pumppals.pumppalsapi.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<PPUser>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<Optional<PPUser>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PostMapping ("/signup")
    public ResponseEntity<PPUser> createUser(@RequestBody PPUser user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    public class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }
}
