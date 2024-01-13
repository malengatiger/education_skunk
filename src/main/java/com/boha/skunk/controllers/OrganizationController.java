package com.boha.skunk.controllers;

import com.boha.skunk.data.Organization;
import com.boha.skunk.data.Pricing;
import com.boha.skunk.data.User;
import com.boha.skunk.services.OrganizationService;
import com.boha.skunk.services.UserBatchService;
import com.boha.skunk.services.WebCrawlerService;
import com.boha.skunk.util.CustomErrorResponse;
import com.boha.skunk.util.E;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
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
    private final UserBatchService userBatchService;
    private final WebCrawlerService webCrawlerService;
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "OrganizationController \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(OrganizationController.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    @PostMapping("/registerOrganization")
    public ResponseEntity<Object> registerOrganization(@RequestBody Organization organization) {
        try {
            Organization result = organizationService.addOrganization(organization);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/updateOrganization")
    public ResponseEntity<Object> updateOrganization(@RequestBody Organization organization) {
        try {
            Organization result = organizationService.updateOrganization(organization);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/crawlWebPage")
    public ResponseEntity<Object> crawlWebPage(@RequestParam String url) {
        try {
            List<WebCrawlerService.TitleLinkPair> linkPairs =
                    webCrawlerService.crawlWebPage("https://www.saexampapers.co.za/grade-12-mathematics/");
            return ResponseEntity.ok(linkPairs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getOrganizations")
    public ResponseEntity<Object> getOrganizations(@RequestParam Long countryId) {
        try {
            List<Organization> pricings = organizationService.getOrganizations();
            return ResponseEntity.ok(pricings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
