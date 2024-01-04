package com.boha.skunk.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class DirectoryUtils {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
            "DirectoryUtils \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(DirectoryUtils.class.getSimpleName());

    public static void deleteFilesInDirectories() {
        List<String> dirs = new ArrayList<>();
        dirs.add("answer_page_images");
        dirs.add("cloudstorage");
        dirs.add("exam_page_images");
        dirs.add("pdfs");

        for (String dir : dirs) {
            File directory1 = new File(dir);
            if (directory1.isDirectory()) {
                File[] files = directory1.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            removeFiles(List.of(Objects.requireNonNull(file.listFiles())));
                            var ok = file.delete();
                            logger.info(mm + "Directory deleted: " + ok +
                                    " \uD83D\uDD35 file: " + file.getAbsolutePath());// Delete the file
                        } else {
                            long lastModifiedTime = file.lastModified();
                            long currentTime = System.currentTimeMillis();
                            long timeDifference = currentTime - lastModifiedTime;
                            long fiveMinutesInMillis = 5 * 60 * 1000L; // 5 minutes in milliseconds

                            if (timeDifference > fiveMinutesInMillis) {
                                var ok = file.delete();
                                logger.info(mm + "File deleted: " + ok +
                                        " \uD83D\uDD35 file: " + file.getAbsolutePath());// Delete the file
                            }
                        }
                    }
                }
            }
        }
    }

    private static void removeFiles(List<File> files) {
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFilesInDirectories(); // Recursively delete files in subdirectories
                } else {
                    long lastModifiedTime = file.lastModified();
                    long currentTime = System.currentTimeMillis();
                    long timeDifference = currentTime - lastModifiedTime;
                    long fiveMinutesInMillis = 5 * 60 * 1000L; // 5 minutes in milliseconds

                    if (timeDifference > fiveMinutesInMillis) {
                        var ok = file.delete(); // Delete the file
                        logger.info(mm + "File deleted: " + ok +
                                " \uD83D\uDD35 file: " + file.getAbsolutePath());// Delete the file

                    }
                }
            }
        }
    }
    public static File createDirectoryIfNotExists(String directoryName) {
        String rootPath = System.getProperty("user.dir");
        String directoryPath = rootPath + File.separator + directoryName;

        File directory = new File(directoryPath);

        // Check if the directory exists
        if (!directory.exists()) {
            // Create the directory
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create the directory: " + directoryPath);
            }
        }

        return directory;
    }
}
