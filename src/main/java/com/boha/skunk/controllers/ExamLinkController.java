package com.boha.skunk.controllers;

import com.boha.skunk.data.*;
import com.boha.skunk.services.ExamLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class ExamLinkController {
    static final String mm = " \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E " +
            "ExamLinkController  \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(ExamLinkController.class.getSimpleName());

    private final ExamLinkService examLinkService;

    @GetMapping("/getSubjects")
    public ResponseEntity<List<Subject>> getSubjects() {
        logger.info(mm + "find all subjects ..... ");

        var list = examLinkService.getSubjects();
        logger.info(mm + "subjects found: " + list.size());
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/addResponseRating")
    public ResponseEntity<GeminiResponseRating> addResponseRating(@RequestBody GeminiResponseRating rating) {
        logger.info(mm + "add response rating ..... ");

        var mRating = examLinkService.addResponseRating(rating);
        return ResponseEntity.ok().body(mRating);
    }

    @GetMapping("/getResponseRatings")
    public ResponseEntity<List<GeminiResponseRating>> getResponseRatings(
            @RequestParam Long examLinkId) throws Exception {
        logger.info(mm + "find response ratings ..... ");

        var list = examLinkService.getExamRatings(examLinkId);
        logger.info(mm + "response ratings found: " + list.size());
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/getExamDocuments")
    public ResponseEntity<List<ExamDocument>> getExamDocuments() {

        var list = examLinkService.getExamDocuments();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getSubjectExamLinks")
    public ResponseEntity<List<ExamLink>> getSubjectExamLinks(@RequestParam Long subjectId) {

        var list = examLinkService.getSubjectExamLinks(subjectId);

        logger.info(mm + "exam links found: " + list.size());

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getExamPageImages")
    public ResponseEntity<List<ExamPageImage>> getExamPageImages(@RequestParam Long examLinkId) {
        logger.info(mm + "............... getExamPageImages ..... ");

        try {
            var list = examLinkService.getExamPageImages(examLinkId);
            return ResponseEntity.ok().body(list);

        } catch (Exception e) {
            logger.severe(mm + "Error getting exam page images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getAnswerPageImages")
    public ResponseEntity<List<AnswerPageImage>> getAnswerPageImages(@RequestParam Long examDocumentId) {
        logger.info(mm + "............... getExamPageImages ..... ");

        try {
            var list = examLinkService.getAnswerPageImages(examDocumentId);
            return ResponseEntity.ok().body(list);

        } catch (Exception e) {
            logger.severe(mm + "Error getting answer page images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
        return file;
    }

}
