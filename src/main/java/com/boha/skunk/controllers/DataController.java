package com.boha.skunk.controllers;

import com.boha.skunk.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {
    static final String mm = "\uD83D\uDC9C \uD83D\uDC9C \uD83D\uDC9C DataController  \uD83D\uDC9B";
    static final Logger logger = Logger.getLogger(DataController.class.getSimpleName());

    private final DataService dataService;


    @GetMapping("extractExamPaperText")
    public ResponseEntity<Object> extractExamPaperText(
            @RequestParam Long examLinkId) throws Exception {
        logger.info(mm+" extractExamPaperText starting: examLinkId: " + examLinkId
                + "  \uD83D\uDD90\uD83C\uDFFE");
        try {
            String text = dataService.extractExamPaperText(examLinkId);
            logger.info(mm+" extractExamPaperText completed: examLinkId: " + examLinkId
                    + "  \uD83D\uDD90\uD83C\uDFFE");
            return ResponseEntity.ok(text);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e);
        }
    }

}
