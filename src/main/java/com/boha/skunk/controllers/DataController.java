package com.boha.skunk.controllers;

import com.boha.skunk.data.ExamPaper;
import com.boha.skunk.data.Image;
import com.boha.skunk.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {
    static final String mm = "\uD83D\uDC9C \uD83D\uDC9C \uD83D\uDC9C DataController  \uD83D\uDC9B";
    static final Logger logger = Logger.getLogger(DataController.class.getSimpleName());

    private final DataService dataService;
    @Value("${educUrl}")
    private String educUrl;


    @GetMapping("extractExamPaper")
    public ExamPaper extractExamPaper(@RequestParam Long examId) throws Exception {
        var text = dataService.extractExamPaper(examId);
        logger.info(mm+" getExamText added ExamPaper, id: " + text.getId() + "  \uD83D\uDD90\uD83C\uDFFE");
        return text;
    }
    @GetMapping("getExamPaper")
    public ResponseEntity<Object> getExamPaper(@RequestParam Long examPaperId) throws Exception {
        ExamPaper examPaper = dataService.getExamPaper(examPaperId);
        if (examPaper != null) {
            logger.info(mm + " getExamPaper found: " + examPaper.getTitle() + "  \uD83D\uDD90\uD83C\uDFFE");
            return ResponseEntity.ok(examPaper);
        } else {
            var msg = " \uD83D\uDD34 \uD83D\uDD34 \uD83D\uDD34 ExamPaper not found with id: " + examPaperId;
            logger.severe(mm+msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }
    @GetMapping("getExamPaperImages")
    public List<Image> getExamPaperImages(@RequestParam Long examPaperId) {
        List<Image> images = dataService.getExamImages(examPaperId);
        logger.info(mm+" getExamPaperImages found: " + images.size() + "  \uD83D\uDD90\uD83C\uDFFE");
        int cnt = 1;
        for (Image image : images) {
            logger.info(mm+" image: #"+ cnt + " \uD83C\uDF45 id: " + image.getId() + " url: " + image.getDownloadUrl());
            cnt++;
        }
        return images;
    }
}
