package com.boha.skunk.services;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.data.ExamLinkRepository;
import com.boha.skunk.data.GeminiResponseRating;
import com.boha.skunk.data.GeminiResponseRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GeminiResponseService {
    private final GeminiResponseRatingRepository geminiResponseRatingRepository;
    private final ExamLinkRepository examLinkRepository;

    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D GeminiResponseService \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(GeminiResponseService.class.getSimpleName());
    public GeminiResponseRating addRating(GeminiResponseRating rating) {
        var r = geminiResponseRatingRepository.save(rating);
        logger.info(mm+" rating added to db " + r.getRating());
        return r;
    }
    public List<GeminiResponseRating> getExamRatingsByPage(Long examLinkId) {
        return geminiResponseRatingRepository.findByExamLinkId(examLinkId);
    }
    public List<GeminiResponseRating> getPageRatingsByExam(Long examLinkId) throws Exception {
        ExamLink examLink;
        Optional<ExamLink> examLinkOptional = examLinkRepository.findById(examLinkId);
        if (examLinkOptional.isPresent()) {
            examLink = examLinkOptional.get();
        } else {
            throw new Exception("ExamLink not found");
        }

//        var list = examLink.
        return new ArrayList<>();
    }
}
