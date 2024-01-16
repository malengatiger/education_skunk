package com.boha.skunk.controllers;

import com.boha.skunk.data.Subject;
import com.boha.skunk.services.SgelaFirestoreService;
import com.boha.skunk.services.YouTubeService;
import com.boha.skunk.util.CustomErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.logging.Logger;

@RestController
//@RequiredArgsConstructor
public class YouTubeController {
    static final String mm = "\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38 " +
            " YouTubeController  \uD83C\uDF38";
    static final Logger logger = Logger.getLogger(YouTubeController.class.getSimpleName());

    private final YouTubeService youTubeService;
    private final SgelaFirestoreService firestoreService;

    public YouTubeController(YouTubeService youTubeService, SgelaFirestoreService firestoreService) {
        this.youTubeService = youTubeService;
        this.firestoreService = firestoreService;
    }


    @GetMapping("/channels")
    public ResponseEntity<Object> searchChannels(@RequestParam("query") String query,
                                                 @RequestParam("maxResults") Long maxResults,
                                                 @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, "Subject not found", new Date().toString()));
        }
        return ResponseEntity.ok().body(youTubeService.searchChannels(query, maxResults, subject));
    }

    @GetMapping("/playlists")
    public ResponseEntity<Object> searchPlaylists(@RequestParam("query") String query,
                                                  @RequestParam("maxResults") Long maxResults,
                                                  @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, "Subject not found", new Date().toString()));
        }
        return ResponseEntity.ok().body(
                youTubeService.searchPlaylists(query, maxResults, subject));
    }

    @GetMapping("/searchVideos")
    public ResponseEntity<Object> searchVideos(@RequestParam("query") String query,
                                               @RequestParam("maxResults") Long maxResults,
                                               @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, "Subject has not been found", new Date().toString()));
        }
        var list = youTubeService.searchVideos(query, maxResults, subject);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/searchVideosByTag")
    public ResponseEntity<Object> searchVideosByTag(@RequestParam("maxResults") Integer maxResults,
                                                    @RequestParam("subjectId") Long subjectId,
                                                    @RequestParam("tagType") Integer tagType) throws Exception {

        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, "Subject not found", new Date().toString()));
        }
        logger.info(mm + " .... searchVideosByTag, subject: " + subject.getTitle());
        return ResponseEntity.ok().body(youTubeService.searchVideosByTag(maxResults.longValue(), subject, tagType));
    }

    private Subject getSubjectById(Long subjectId) throws Exception {
        return firestoreService.getSubjectById(subjectId);
    }
}