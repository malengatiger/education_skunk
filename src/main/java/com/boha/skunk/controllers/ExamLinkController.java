package com.boha.skunk.controllers;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.data.Subject;
import com.boha.skunk.data.SubjectRepository;
import com.boha.skunk.data.Tag;
import com.boha.skunk.services.ExamLinkService;
import com.boha.skunk.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
        var list = examLinkService.getSubjects();
        logger.info(mm+"subjects found: " + list.size());
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/getSubjectExamLinks")
    public ResponseEntity<List<ExamLink>> getSubjectExamLinks(@RequestParam Long subjectId) {
        var list = examLinkService.getSubjectExamLinks(subjectId);
        logger.info(mm+"exam links found: " + list.size());

        return ResponseEntity.ok().body(list);
    }
}
