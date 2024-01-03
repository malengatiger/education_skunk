package com.boha.skunk.services;


import com.boha.skunk.data.Organization;
import com.boha.skunk.data.OrganizationRepository;
import com.boha.skunk.data.User;
import com.boha.skunk.util.FileToUsers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserBatchService {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBatchService.class);

    private final UserService userService;
    private final OrganizationRepository organizationRepository;

    public  List<User> handleUsersFromJSON(File file, Long organizationId
                                           ) throws Exception {
        Optional<Organization> optionalOrg = organizationRepository.findById(organizationId);
        Organization org = optionalOrg.orElseThrow(() -> new Exception("Organization not found")); // Handle the case when the organization is not found
        List<User> users = FileToUsers.getUsersFromJSONFile(file);
        return processUsers(org, users);
    }

    private  List<User> processUsers(Organization org,
                                     List<User> users) throws Exception {
        List<User> usersCreated = new ArrayList<>();
        LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 processUsers: " + users.size() + " users");
        for (User user : users) {
            user.setOrganization(org);
            user.setActiveFlag(true);

            userService.createUser(user);
            usersCreated.add(user);
            LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " +
                    "processUsers: user created " + user.getName());
        }
        LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " +
                "processUsers: users created " + usersCreated.size() + " users");
        return usersCreated;
    }

    public  List<User> handleUsersFromCSV(File file,Long organizationId) throws Exception {
        Optional<Organization> optionalOrg = organizationRepository.findById(organizationId);
        Organization org = optionalOrg.orElseThrow(() -> new Exception("Organization not found")); // Handle the case when the organization is not found
        List<User> users = FileToUsers.getUsersFromCSVFile(file);
        return processUsers(org, users);
    }
}
