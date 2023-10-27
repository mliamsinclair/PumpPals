package com.pumppals.pumppalsapi.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pumppals.pumppalsapi.model.PPUser;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PPUser> tempUser = userService.getUserByUsername(username);
        if (tempUser.isPresent()) {
            PPUser user = tempUser.get();
            return new User(user.getUsername(), user.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}