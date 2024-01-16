package com.boha.skunk.controllers;

import com.boha.skunk.data.User;
import com.boha.skunk.services.UserBatchService;
import com.boha.skunk.services.UserService;
import com.boha.skunk.services.WebCrawlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserBatchService userBatchService;
    private final WebCrawlerService webCrawlerService;
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "UserController \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(UserController.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public UserController(UserService userService, UserBatchService userBatchService, WebCrawlerService webCrawlerService) {
        this.userService = userService;
        this.userBatchService = userBatchService;
        this.webCrawlerService = webCrawlerService;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            User result = userService.createUser(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        try {
            User result = userService.updateUser(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getOrganizationUsers(@RequestParam Long organizationId) {
        try {
            List<User> organizationUsers = userService.getOrganizationUsers(organizationId);
            return ResponseEntity.ok(organizationUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
