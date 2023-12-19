package com.boha.skunk.controllers;

import com.boha.skunk.data.ExamImage;
import com.boha.skunk.data.ExamPaper;
import com.boha.skunk.services.DataService;
import com.boha.skunk.services.TableExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {
    static final String mm = "\uD83D\uDC9C \uD83D\uDC9C \uD83D\uDC9C DataController  \uD83D\uDC9B";
    static final Logger logger = Logger.getLogger(DataController.class.getSimpleName());

    private final DataService dataService;
    private final TableExtractorService tableExtractorService;
    @Value("${educUrl}")
    private String educUrl;


    @GetMapping("extractExamPaper")
    public ResponseEntity<Object> extractExamPaper(@RequestParam Long examId) throws Exception {
        ExamPaper examPaper;
        try {
            examPaper = dataService.extractExamPaper(examId);
            logger.info(mm+" getExamText added ExamPaper, id: " + examPaper.getId() + "  \uD83D\uDD90\uD83C\uDFFE");
            return ResponseEntity.ok(examPaper);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e);
        }
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
    public List<ExamImage> getExamPaperImages(@RequestParam Long examPaperId) {
        List<ExamImage> examImages = dataService.getExamImages(examPaperId);
        logger.info(mm+" getExamPaperImages found: " + examImages.size() + "  \uD83D\uDD90\uD83C\uDFFE");
        int cnt = 1;
        for (ExamImage examImage : examImages) {
            logger.info(mm+" image: #"+ cnt + " \uD83C\uDF45 id: " + examImage.getId() + " url: " + examImage.getDownloadUrl());
            cnt++;
        }
        return examImages;
    }
}
