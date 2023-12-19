package com.boha.skunk.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class CloudStorageService {

    private final Storage storage;
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D CloudStorageService \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(LinkExtractorService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    public CloudStorageService() throws IOException {
        // Initialize Firebase Admin SDK and get the storage instance using default credentials
        storage = StorageOptions.getDefaultInstance().getService();
        logger.info(mm+" Storage initialized : " + storage.toString());
    }

    @Value("${projectId}")
    private String projectId;
    @Value("${storageBucket}")
    private String bucketName;
    @Value("${cloudStorageDirectory}")
    private String cloudStorageDirectory;
    public String uploadFile(File file) throws IOException {

        logger.info(mm +
                " ............. uploadFile to cloud storage: " + file.getName());
        String contentType = Files.probeContentType(file.toPath());
//        Storage storage = StorageOptions.newBuilder()
//                .setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, cloudStorageDirectory
                + "/" + file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        logger.info(mm +
                " uploadFile to cloud storage, contentType: " + contentType);

        Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()));

        // Generate a signed URL for the blob with no permissions required
        String downloadUrl = String.valueOf(blob.signUrl(3650, TimeUnit.DAYS, Storage.SignUrlOption.withV2Signature()));
        logger.info(mm +
                " file uploaded to cloud storage ");
        return downloadUrl;
    }

}