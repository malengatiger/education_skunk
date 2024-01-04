package com.boha.skunk.services;

import com.boha.skunk.data.Organization;
import com.boha.skunk.data.User;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
            "OrganizationService \uD83D\uDC9C";
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    static final Logger logger = Logger.getLogger(OrganizationService.class.getSimpleName());


    private final MailService mailService;
//
//    public List<Organization> getAllOrganizations() {
//        return organizationRepository.findAll();
//    }
//
//    public Optional<Organization> getOrganizationById(Long id) {
//        return organizationRepository.findById(id);
//    }
//
//    public Organization createOrganization(Organization organization) {
//        Organization org  = organizationRepository.save(organization);
//        return org;
//    }
//
//    public Organization updateOrganization(Long id, Organization updatedOrganization) {
//        Optional<Organization> organizationOptional = organizationRepository.findById(id);
//        if (organizationOptional.isPresent()) {
//            Organization organization = organizationOptional.get();
//            organization.setName(updatedOrganization.getName());
//            organization.setEmail(updatedOrganization.getEmail());
//            organization.setCellphone(updatedOrganization.getCellphone());
//            return organizationRepository.save(organization);
//        }
//        return null; // or throw an exception if desired
//    }
//
//    public void deleteOrganization(Long id) {
//        organizationRepository.deleteById(id);
//    }
//
//    // User methods
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public Optional<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }
//
//
//    public User registerUser(User user) throws Exception {
//        logger.info("\uD83E\uDDE1\uD83E\uDDE1 create user : " +user.getName());
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        logger.info("\uD83E\uDDE1\uD83E\uDDE1 createRequest  .... ");
//        String password = UUID.randomUUID().toString();
//        User mUser;
//        try {
//            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest();
//            createRequest.setPhoneNumber(user.getCellphone());
//            createRequest.setDisplayName(user.getName());
//            createRequest.setPassword(password); //todo - refactor this!!
//            createRequest.setEmail(user.getEmail());
//
//            logger.info("\uD83E\uDDE1\uD83E\uDDE1 createUserAsync  .... ");
//
//
//            ApiFuture<UserRecord> userRecord = firebaseAuth.createUserAsync(createRequest);
//            logger.info("\uD83E\uDDE1\uD83E\uDDE1 userRecord : " + G.toJson(userRecord));
//            String uid = userRecord.get().getUid();
//            user.setFirebaseUserId(uid);
//            mUser = userRepository.save(user);
//
//            String message = "Dear " + user.getName() +
//                    "      ,\n\nYou have been registered with GeoMonitor and the team is happy to send you the first time login password. '\n" +
//                    "      \nPlease login on the web with your email and the attached password ("+password+"). Use your cellphone number to sign in on the phone.\n" +
//                    "      \n\nThank you for working with SgelaAI. \nWelcome aboard!!\nBest Regards,\nThe SgelaAI Team\ninfo@geomonitorapp.io\n\n";
//
//            logger.info("\uD83E\uDDE1\uD83E\uDDE1 sending email  .... ");
//
//            mailService.sendHtmlEmail(user.getEmail(), message, "Welcome to SgelaAI");
//            logger.info("\uD83E\uDDE1\uD83E\uDDE1 create user completed. ");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//
//        return mUser;
//    }
//
//    public int updateAuthedUser(User user) throws Exception {
//        String password = UUID.randomUUID().toString();
//        try {
//            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(user.getFirebaseUserId())
//                    .setEmail(user.getEmail())
//                    .setPhoneNumber(user.getCellphone())
//                    .setEmailVerified(false)
//                    .setPassword(password)
//                    .setDisplayName(user.getName())
//                    .setDisabled(false);
//
//            FirebaseAuth.getInstance().updateUser(request);
//            return 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("User auth update failed: " + e.getMessage());
//        }
//
//    }
//    public User updateUser(Long id, User updatedUser) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setFirstName(updatedUser.getFirstName());
//            user.setLastName(updatedUser.getLastName());
//            user.setEmail(updatedUser.getEmail());
//            user.setCellphone(updatedUser.getCellphone());
//            user.setActiveFlag(updatedUser.isActiveFlag());
//            return userRepository.save(user);
//        }
//        return null; // or throw an exception if desired
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//    public List<User> getOrganizationUsers(Long organizationId) {
//        return userRepository.findByOrganizationId(organizationId);
//    }
}