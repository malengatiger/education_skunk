package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
//@RequiredArgsConstructor
public class ExamLinkService {


    private final CloudStorageService cloudStorageService;
    static final String mm = "\uD83C\uDF3F\uD83C\uDF3F\uD83C\uDF3F ExamLinkService \uD83E\uDD43";

    static final Logger logger = Logger.getLogger(ExamLinkService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Value("${educUrl}")
    private String educUrl;

    private final SgelaFirestoreService firestoreService;

    public ExamLinkService(CloudStorageService cloudStorageService, SgelaFirestoreService firestoreService) {
        this.cloudStorageService = cloudStorageService;
        this.firestoreService = firestoreService;
    }


    public String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
    public List<File> extractFilesFromZip(File zipFile) throws IOException {
        List<File> extractedFiles = new ArrayList<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File extractedFile = new File(entry.getName());
                    extractedFiles.add(extractedFile);

                    try (OutputStream outputStream = new FileOutputStream(extractedFile)) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }

        return extractedFiles;
    }

    public List<ExamLink> getSubjectExamLinks(Long subjectId) throws Exception {

        return firestoreService.getSubjectExamLinks(subjectId);
    }


    public List<Subject> getSubjects() throws Exception {
        logger.info(mm + " ... getSubjects ...");
        return firestoreService.getSubjects();
    }

    public List<GeminiResponseRating> getExamRatings(Long examLinkId) throws Exception {

        List<GeminiResponseRating> ratings = firestoreService.getResponseRatings(examLinkId);

        logger.info(mm + " ... getExamRatings ... found: " + ratings.size());
        return ratings;
    }


    public List<String> addResponseRating(GeminiResponseRating responseRating) throws Exception {
        logger.info(mm+"adding rating: " + G.toJson(responseRating));
        List<GeminiResponseRating> ratings = new ArrayList<>();
        ratings.add(responseRating);
        var res = firestoreService.addGeminiResponseRatings(ratings);
        logger.info(mm + " ... ResponseRating added to db ...");
        return res;
    }

    public static class DistinctLink {
        Long id;
        String title;

        public DistinctLink(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}