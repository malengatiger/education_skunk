package com.boha.skunk.controllers;

import com.boha.skunk.data.Organization;
import com.boha.skunk.data.User;
import com.boha.skunk.services.OrganizationService;
import com.boha.skunk.services.UserBatchService;
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
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "OrganizationController \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(OrganizationController.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

}
