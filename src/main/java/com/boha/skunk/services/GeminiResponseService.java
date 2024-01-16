package com.boha.skunk.services;

import com.boha.skunk.data.GeminiResponseRating;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
//@RequiredArgsConstructor
public class GeminiResponseService {
    private final SgelaFirestoreService firestoreService;

    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "GeminiResponseService \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(GeminiResponseService.class.getSimpleName());

    public GeminiResponseService(SgelaFirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public List<String> addRating(GeminiResponseRating rating) throws Exception {
        List<GeminiResponseRating> ratings = new ArrayList<>();
        ratings.add(rating);
        var r = firestoreService.addGeminiResponseRatings(ratings);
        logger.info(mm+" rating added to db " );
        return r;
    }
    public List<GeminiResponseRating> getResponseRatings(Long examLinkId) throws Exception {
        return firestoreService.getResponseRatings(examLinkId);
    }

}
