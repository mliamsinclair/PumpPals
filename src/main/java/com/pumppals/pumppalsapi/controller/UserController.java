package com.pumppals.pumppalsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import com.pumppals.pumppalsapi.model.AuthRequest;
import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.service.JwtService;
import com.pumppals.pumppalsapi.service.UserService;

import java.util.List;
import java.util.Optional;

// controller to handle user requests
@RestController
@RequestMapping("/api")
public class UserController {
    
    // user service to handle user requests
    @Autowired
    private UserService userService;

    // authentication manager to authenticate user
    @Autowired
    private AuthenticationManager authenticationManager;

    // JWT service to generate JWT
    @Autowired
    private JwtService jwtService;

    // get all users
    @GetMapping(value = "/all")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    // get user by username
    @GetMapping("/{username}")
    public ResponseEntity<Optional<UserInfo>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    // create user
    @PostMapping("/create")
    public ResponseEntity<UserInfo> createUser(@RequestBody UserInfo user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    // authenticate user and generate JWT
    // JWT is used to authenticate user for all other requests
    // JWT is passed in header of all other requests
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }

    // exception to handle username already exists
    public class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }
}
