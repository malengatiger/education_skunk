package com.boha.skunk.services;

import com.boha.skunk.util.DirectoryUtils;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        logger.info(mm + " Storage initialized : " + storage.toString());
    }

    @Value("${projectId}")
    private String projectId;
    @Value("${storageBucket}")
    private String bucketName;
    @Value("${cloudStorageDirectory}")
    private String cloudStorageDirectory;

    public File downloadFile(String url) throws Exception {
        File dir = DirectoryUtils.createDirectoryIfNotExists("pdfs");
        String path = dir.getPath() + "/images_"
                + System.currentTimeMillis() + ".zip";
        File file = new File(path);
        FileUtils.copyURLToFile(new URL(url),
               file);

        logger.info(mm+"File downloaded from Cloud Storage: "
                + (file.length()/1024) + "K bytes");
        return file;
    }

    public String uploadFile(File file,Long examLinkId) throws IOException {

        logger.info(mm +
                " ............. uploadFile to cloud storage: " + file.getName());
        String contentType = Files.probeContentType(file.toPath());
        BlobId blobId = BlobId.of(bucketName, cloudStorageDirectory
                + "/sgelaAI_examLink_" + examLinkId + "_" + System.currentTimeMillis() + ".png");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        logger.info(mm +
                " uploadFile to cloud storage, contentType: " + contentType);

        Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()));

        // Generate a signed URL for the blob with no permissions required
        String downloadUrl = String.valueOf(blob.signUrl(3650, TimeUnit.DAYS, Storage.SignUrlOption.withV2Signature()));
        logger.info(mm +
                " file uploaded to cloud storage: \n" + downloadUrl);
        return downloadUrl;
    }

}