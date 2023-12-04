package com.pumppals.pumppalsapi.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pumppals.pumppalsapi.exceptions.FileDownloadException;
import com.pumppals.pumppalsapi.model.AuthRequest;
import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.service.FileService;
import com.pumppals.pumppalsapi.service.JwtService;
import com.pumppals.pumppalsapi.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Optional<UserInfo>> getUserByUsername(@PathVariable String username) {
        System.out.println(username);
        System.out.println(userService.getUserByUsername(username));
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserInfo user) {
        System.out.println("Creating user: " + user.getUsername());
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password cannot be empty.");
        }
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            // return bad request if username already exists
            return ResponseEntity.badRequest().body("Username already exists.");
        }
        userService.createUser(user);
        return ResponseEntity.ok().body("User created successfully.");
    }

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

    @GetMapping("/refresh")
    public String refreshToken(Principal principal) {
        return jwtService.generateToken(principal.getName());
    }

    @PutMapping("/pfp")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, Principal principal) {
        String username = principal.getName();
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        System.out.println("Uploading profile picture for " + username + " with extension " + fileExtension);
        if (fileExtension != null
                && (fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals("jpeg"))) {
            try {
                // set file name to username + .png
                String fileName = username + ".png";
                
                // upload original image to s3
                fileService.uploadFile(file, fileName);

                return ResponseEntity.ok().body("Profile picture uploaded and cropped successfully.");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload and crop profile picture.");
            }
        } else {
            return ResponseEntity.badRequest()
                    .body("Invalid file format. Only PNG, JPG, and JPEG formats are allowed.");
        }
    }

    // get profile picture
    @GetMapping("/pfp/{username}")
    public ResponseEntity<?> getProfilePicture(@PathVariable String username) {
        System.out.println("Getting profile picture for " + username);
        try {
            UrlResource file = (UrlResource) fileService.downloadFile(username);
            if (file != null) {
                // Read the image into a BufferedImage
                BufferedImage originalImage = ImageIO.read(file.getInputStream());

                // Determine the dimensions of the square
                int squareSize = Math.min(originalImage.getWidth(), originalImage.getHeight());

                // Calculate the coordinates of the square
                int xc = originalImage.getWidth() / 2;
                int yc = originalImage.getHeight() / 2;

                // Crop the image
                BufferedImage croppedImage = originalImage.getSubimage(
                        xc - (squareSize / 2), // x-coordinate of the upper-left corner
                        yc - (squareSize / 2), // y-coordinate of the upper-left corner
                        squareSize, // width of the rectangle
                        squareSize // height of the rectangle
                );

                // Write the cropped image to a ByteArrayOutputStream
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(croppedImage, "png", baos);
                baos.flush();
                byte[] croppedBytes = baos.toByteArray();
                baos.close();

                // Delete the local file
                file.getFile().delete();

                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(croppedBytes);
            } else {
                return ResponseEntity.badRequest().body("File does not exist.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve profile picture.");
        } catch (FileDownloadException e) {
            return ResponseEntity.badRequest().body("File does not exist.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve profile picture.");
        }
    }

    // get user by token
    @GetMapping("/user/username")
    public ResponseEntity<String> getUserByToken(Principal principal) {
        System.out.println("Getting user by token " + principal.getName());
        return ResponseEntity.ok().body(principal.getName());
    }

    // update user
    @PutMapping("/user/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserInfo user, Principal principal) {
        System.out.println("Updating user " + principal.getName());
        user.setUsername(principal.getName());
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    // follow user
    @PutMapping("/user/follow/{username}")
    public ResponseEntity<Boolean> followUser(@PathVariable String username, Principal principal) {
        System.out.println("Following user " + username + " as " + principal.getName());
        return ResponseEntity.ok().body(userService.followUser(principal.getName(), username));
    }

    // unfollow user
    @PutMapping("/user/unfollow/{username}")
    public ResponseEntity<Boolean> unfollowUser(@PathVariable String username, Principal principal) {
        System.out.println("Unfollowing user " + username + " as " + principal.getName());
        return ResponseEntity.ok().body(userService.unfollowUser(principal.getName(), username));
    }

    // check if user is following another user
    @GetMapping("/user/checkfollow/{username}")
    public ResponseEntity<Boolean> checkFollow(@PathVariable String username, Principal principal) {
        System.out.println("Checking if " + principal.getName() + " is following " + username);
        return ResponseEntity.ok().body(userService.checkFollow(principal.getName(), username));
    }

    // return a list of recommended users
    @GetMapping("/user/recommended")
    public ResponseEntity<List<UserInfo>> getRecommendedUsers(Principal principal) {
        System.out.println("Getting recommended users for " + principal.getName());
        return ResponseEntity.ok().body(userService.getRecommendedUsers(principal.getName()));
    }

    public class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }
}
