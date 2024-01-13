package com.boha.skunk.services;

import com.boha.skunk.data.Organization;
import com.boha.skunk.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
            "OrganizationService \uD83D\uDC9C";
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    static final Logger logger = Logger.getLogger(OrganizationService.class.getSimpleName());
    private final SgelaFirestoreService sgelaFirestoreService;

    private final MailService mailService;
    private final UserService userService;

    //
    public List<Organization> getOrganizations() throws Exception {
        return sgelaFirestoreService.getAllDocuments(Organization.class);
    }

    public Organization addOrganization(Organization organization) throws Exception {
        var mUser = userService.createUser(organization.getAdminUser());
        organization.setAdminUser(mUser);
        sgelaFirestoreService.addDocument(organization);
        return sgelaFirestoreService.getOrganization(organization.getId());
    }

    public Organization updateOrganization(Organization organization) throws Exception {
        sgelaFirestoreService.updateDocument(Organization.class.getSimpleName(),
                organization.getId(), Util.objectToMap(organization));
        return sgelaFirestoreService.getDocument(Organization.class.getSimpleName(),
                organization.getId(), Organization.class);
    }
}