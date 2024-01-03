package com.boha.skunk.controllers;

import com.boha.skunk.data.Organization;
import com.boha.skunk.data.User;
import com.boha.skunk.data.UserRepository;
import com.boha.skunk.services.OrganizationService;
import com.boha.skunk.services.UserBatchService;
import com.boha.skunk.services.YouTubeService;
import com.boha.skunk.util.CustomErrorResponse;
import com.boha.skunk.util.E;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final UserRepository userRepository;
    private final UserBatchService userBatchService;
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "OrganizationController \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(OrganizationController.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();


    @PostMapping("uploadMemberFile")
    public ResponseEntity<Object> uploadMemberFile(
            @RequestParam Long organizationId,
            @RequestPart MultipartFile document) throws IOException {

        List<User> users = new ArrayList<>();
        String doc = document.getOriginalFilename();
        if (doc == null) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "Problem with user file ",
                            new DateTime().toDateTimeISO().toString()));
        }
        File file = new File(doc);
        Files.write(document.getBytes(), file);
        logger.info("\uD83C\uDF3C\uD83C\uDF3C we have a file: " + file.getName());
        if (file.getName().contains(".csv")) {
            logger.info("\uD83C\uDF3C\uD83C\uDF3C csv file to process: " + file.getName());
            try {
                users = userBatchService.handleUsersFromCSV(file, organizationId);
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(400,
                                "Failed to create users: " + e.getMessage(),
                                new DateTime().toDateTimeISO().toString()));
            }
        }
        if (file.getName().contains(".json")) {
            logger.info("\uD83C\uDF3C\uD83C\uDF3C json file to process: " + file.getName());
            try {
                users = userBatchService.handleUsersFromJSON(file, organizationId);
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(400,
                                "Failed to create users: " + e.getMessage(),
                                new DateTime().toDateTimeISO().toString()));
            }
        }
        if (file.exists()) {
            boolean ok = file.delete();
            if (ok) {
                logger.info(E.RED_APPLE + E.RED_APPLE +
                        " user batch file deleted");
            }
        }
        logger.info("\uD83C\uDF3C\uD83C\uDF3C no users created ... wtf ");
        return ResponseEntity.badRequest().body(
                new CustomErrorResponse(400,
                        "Failed to create users; no users in file or file is not .json or .csv ",
                        new DateTime().toDateTimeISO().toString()));
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return new ResponseEntity<>(organizations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationService.getOrganizationById(id);
        return organizationOptional.map(organization -> new ResponseEntity<>(organization, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("registerOrganization")
    public ResponseEntity<Object> registerOrganization(@RequestBody Organization organization) {
        try {
            Organization createdOrganization = organizationService.createOrganization(organization);
            return new ResponseEntity<>(createdOrganization, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400,e.getMessage(), new Date().toString()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id, @RequestBody Organization updatedOrganization) {
        Organization organization = organizationService.updateOrganization(id, updatedOrganization);
        if (organization != null) {
            return new ResponseEntity<>(organization, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // User methods

    @GetMapping("/getOrganizationUsers")
    public ResponseEntity<List<User>> getOrganizationUsers(
            @RequestParam Long organizationId) {
        List<User> users = organizationService.getOrganizationUsers(organizationId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            var u = organizationService.registerUser(user);
            return ResponseEntity.ok().body(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,e.getMessage(),new Date().toString()));
        }
    }

    @PostMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User oldUser = userOptional.get();
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setEmail(user.getEmail());
            oldUser.setCellphone(user.getCellphone());
            User updatedUser = userRepository.save(oldUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
