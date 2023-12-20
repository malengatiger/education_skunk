package com.boha.skunk.services;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.data.ExamLinkRepository;
import com.boha.skunk.data.Subject;
import com.boha.skunk.data.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ExamLinkService {
    private final ExamLinkRepository examLinkRepository;
    private final SubjectRepository subjectRepository;
    static final String mm = "\uD83C\uDF3F\uD83C\uDF3F\uD83C\uDF3F ExamLinkService \uD83E\uDD43";

    static final Logger logger = Logger.getLogger(ExamLinkService.class.getSimpleName());
    @Value("${educUrl}")
    private String educUrl;


    public ExamLink saveExamLink(ExamLink examLink) {
        try {
            var res = examLinkRepository.save(examLink);
            logger.info(mm + " ExamLink saved in database: " + examLink.getDocumentTitle()
                    + " - " + examLink.getTitle() + " link: " + examLink.getLink() + " \uD83D\uDD35\uD83D\uDD35 id: " + res.getId());
            return res;
        } catch (Exception e) {
            logger.severe(mm+"Error saving ExamLink to database: " + e.getMessage());
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
        return subjectRepository.findAll();
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