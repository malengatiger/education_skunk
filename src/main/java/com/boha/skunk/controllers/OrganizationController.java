package com.boha.skunk.controllers;

import com.boha.skunk.data.Organization;
import com.boha.skunk.data.User;
import com.boha.skunk.data.UserRepository;
import com.boha.skunk.services.OrganizationService;
import com.boha.skunk.util.CustomErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final UserRepository userRepository;


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
