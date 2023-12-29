package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class ExamLinkService {
    private final ExamLinkRepository examLinkRepository;
    private final SubjectRepository subjectRepository;
    private final GeminiResponseRatingRepository geminiResponseRatingRepository;
    private final ExamPageImageRepository examPageImageRepository;
    private final CloudStorageService cloudStorageService;
    static final String mm = "\uD83C\uDF3F\uD83C\uDF3F\uD83C\uDF3F ExamLinkService \uD83E\uDD43";

    static final Logger logger = Logger.getLogger(ExamLinkService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Value("${educUrl}")
    private String educUrl;

    public List<ExamPageImage> getExamPageImages(Long examLinkId) throws Exception {
        List<ExamPageImage> examPageImages = examPageImageRepository.findByExamLinkId(examLinkId);
        if (!examPageImages.isEmpty()) {
            logger.info(mm + "ExamPageImages exist already: \uD83E\uDD66\uD83E\uDD66 "
                    + examPageImages.size() + " pages");
            return examPageImages;
        }
        logger.info(mm + "download zip directory and " +
                "create ExamPageImages ...  \uD83E\uDD66\uD83E\uDD66 ");

        Optional<ExamLink> examLinkOptional = examLinkRepository.findById(examLinkId);

        ExamLink examLink;
        if (examLinkOptional.isPresent()) {
            examLink = examLinkOptional.get();
        } else {
            throw new Exception("ExamLink not found");
        }
        if (examLink.getPageImageZipUrl().isEmpty()) {
            throw new Exception("getPageImageZipUrl is null");
        }
        File zipDir = cloudStorageService.downloadFile(examLink.getPageImageZipUrl());
        List<File> files = extractFilesFromZip(zipDir);
        int index = 0;
        List<ExamPageImage> mList = new ArrayList<>();
        for (File file : files) {
            String url = cloudStorageService.uploadFile(file,examLinkId);
            ExamPageImage epi = new ExamPageImage();
            epi.setExamLink(examLink);
            epi.setDate(DateTime.now().toDateTimeISO().toString());
            epi.setDownloadUrl(url);
            epi.setPageIndex(index);
            epi.setMimeType(getFileType(file));
            mList.add(epi);
            index++;
        }
        logger.info(mm + "ExamPageImages to be added: " + mList.size());
        List<ExamPageImage>  list = examPageImageRepository.saveAll(mList);
        logger.info(mm + "ExamPageImages added OK: \uD83E\uDD66\uD83E\uDD66 "
                + list.size() + " .... returning ...");

        return list;

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

    public ExamLink saveExamLink(ExamLink examLink) {
        try {
            var res = examLinkRepository.save(examLink);
            logger.info(mm + " ExamLink saved in database: " + examLink.getDocumentTitle()
                    + " - " + examLink.getTitle() + " link: " + examLink.getLink() + " \uD83D\uDD35\uD83D\uDD35 id: " + res.getId());
            return res;
        } catch (Exception e) {
            logger.severe(mm + "Error saving ExamLink to database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Object[]> getDistinctDocumentTitles() {
        return examLinkRepository.findDistinctDocumentTitlesWithId();
    }

    public List<ExamLink> getSubjectExamLinks(Long subjectId) {
        return examLinkRepository.findBySubjectId(subjectId);
    }

    public ExamLink getExamLink(Long examLinkId) throws Exception {
        Optional<ExamLink> examLinkOptional = examLinkRepository.findById(examLinkId);
        if (examLinkOptional.isPresent()) {
            return examLinkOptional.get();
        } else {
            throw new Exception("ExamLink not found");
        }

    }

    public List<Subject> getSubjects() {
        logger.info(mm + " ... getSubjects ...");
        return subjectRepository.findAll();
    }

    public List<GeminiResponseRating> getExamRatings(Long examLinkId) {
        List<GeminiResponseRating> ratings = new ArrayList<>();

        List<ExamPageImage> images = examPageImageRepository.findByExamLinkId(examLinkId);
        for (ExamPageImage image : images) {
            var list = geminiResponseRatingRepository.findByExamPageImageId(image.getId());
            ratings.addAll(list);
        }
        logger.info(mm + " ... getExamRatings ... found: " + ratings.size());
        return ratings;
    }

    public List<ExamPageImage> addExamImages(ExamPageImage examPageImage, List<File> imageFiles) throws IOException {
        List<ExamPageImage> list = new ArrayList<>();
        for (File imageFile : imageFiles) {
            String url = cloudStorageService.uploadFile(imageFile, examPageImage.getExamLink().getId());
            examPageImage.setDownloadUrl(url);
            ExamPageImage res = examPageImageRepository.save(examPageImage);
            list.add(res);
            logger.info(mm + " ... ExamImage added to db ...");
        }

        return list;
    }

    public GeminiResponseRating addResponseRating(GeminiResponseRating responseRating) {
        logger.info(mm+"adding rating: " + G.toJson(responseRating));
        var res = geminiResponseRatingRepository.save(responseRating);
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