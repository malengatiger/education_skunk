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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        var proj = storage.getOptions().getProjectId();
        var app = storage.getOptions().getCredentials();
        try {
            logger.info(mm + "CloudStorageService: projectId:" + proj);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        logger.info(mm + "File downloaded from Cloud Storage: "
                + (file.length() / 1024) + "K bytes");
        return file;
    }

    static int EXAM_FILE = 0;
    static int ANSWER_FILE = 1;

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public String uploadFile(File file, Long id, int type) throws IOException {
        File dir = DirectoryUtils.createDirectoryIfNotExists("cloudstorage");
        File mFile = copyAndDeleteFile(file,
                new File(dir.getPath() + "/file_" + id
                        + "_" + System.currentTimeMillis()
                        + "."
                        + getFileExtension(file.getName())));
        String path;
        if (type == EXAM_FILE) {
            path = dir.getPath() + "/sgelaAI_examLink_";
        } else {
            path = dir.getPath() + "/sgelaAI_answerLink_";
        }
        logger.info(mm +
                " ............. uploadFile to cloud storage: " + path + "/" + mFile.getName());
        String contentType = Files.probeContentType(mFile.toPath());
        BlobId blobId = BlobId.of(bucketName, cloudStorageDirectory
                + path + id + "_" + System.currentTimeMillis() + "."
                + getFileExtension(mFile.getName()));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();


        Blob blob = storage.createFrom(blobInfo, Paths.get(mFile.getPath()));

        // Generate a signed URL for the blob with no permissions required
        String downloadUrl = String.valueOf(blob.signUrl(3650, TimeUnit.DAYS, Storage.SignUrlOption.withV2Signature()));
        Files.delete(mFile.toPath());
        logger.info(mm +
                " file uploaded to cloud storage, type: " + type + " \n" + downloadUrl);
        return downloadUrl;
    }

    public File copyAndDeleteFile(File sourceFile, File destinationFile) throws IOException {
        // Copy the file to the destination location
        Path sourcePath = sourceFile.toPath();
        Path destinationPath = destinationFile.toPath();
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Delete the original file
        Files.delete(sourcePath);
        return destinationFile;
    }
}