package com.boha.skunk.controllers;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.data.ExamLinkRepository;
import com.boha.skunk.services.ExamLinkService;
import com.boha.skunk.services.LinkExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkExtractorController {
    static final String mm = "\uD83E\uDD66\uD83E\uDD66\uD83E\uDD66 LinkExtractorController  \uD83D\uDC9B";
    static final Logger logger = Logger.getLogger(LinkExtractorController.class.getSimpleName());

    private final LinkExtractorService linkExtractorService;
    private final ExamLinkRepository examLinkRepository;
    @Value("${educUrl}")
    private String educUrl;


    @GetMapping("extractExamLinks")
    public ResponseEntity<Object> extractExamLinks() {
        try {
            var list = linkExtractorService.extractExamLinks();
            logger.info(mm+" extractExamLinks found: " + list.size() + " examLinks");
            return ResponseEntity.ok(list);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
    @GetMapping("getDistinctLinks")
    public List<ExamLinkService.DistinctLink> getDistinctLinks() {
        var list = examLinkRepository.findDistinctDocumentTitlesWithId();
        List<ExamLinkService.DistinctLink> m = new ArrayList<>();
        for (Object[] objects : list) {
            ExamLinkService.DistinctLink d = new ExamLinkService.DistinctLink((Long) objects[0], (String) objects[1]);
            m.add(d);

        }
        logger.info(mm+" getDistinctLinks found: " + m.size());
        return m;
    }
}
