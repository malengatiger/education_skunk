package com.boha.skunk.controllers;

import com.boha.skunk.data.Subject;
import com.boha.skunk.data.SubjectRepository;
import com.boha.skunk.data.YouTubeData;
import com.boha.skunk.services.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class YouTubeController {
    static final String mm = "\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38 " +
            "YouTubeController  \uD83C\uDF38";
    static final Logger logger = Logger.getLogger(YouTubeController.class.getSimpleName());

    private final YouTubeService youTubeService;
    private final SubjectRepository subjectRepository;

    @Autowired
    public YouTubeController(YouTubeService youTubeService, SubjectRepository subjectRepository) {
        this.youTubeService = youTubeService;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/channels")
    public List<YouTubeData> searchChannels(@RequestParam("query") String query,
                                            @RequestParam("maxResults") Long maxResults,
                                            @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        return youTubeService.searchChannels(query, maxResults, subject);
    }

    @GetMapping("/playlists")
    public List<YouTubeData> searchPlaylists(@RequestParam("query") String query,
                                             @RequestParam("maxResults") Long maxResults,
                                             @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            throw new Exception("Subject not found");
        }
        return youTubeService.searchPlaylists(query, maxResults, subject);
    }

    @GetMapping("/searchVideos")
    public List<YouTubeData> searchVideos(@RequestParam("query") String query,
                                          @RequestParam("maxResults") Long maxResults,
                                          @RequestParam("subjectId") Long subjectId) throws Exception {
        Subject subject = getSubjectById(subjectId);
        if (subject == null) {
            throw new Exception("Subject not found");
        }
        return youTubeService.searchVideos(query, maxResults, subject);
    }
    @GetMapping("/searchVideosByTag")
    public List<YouTubeData> searchVideosByTag(@RequestParam("maxResults") Integer maxResults,
                                               @RequestParam("subjectId") String subjectId,
                                               @RequestParam("tagType") Integer tagType) throws Exception {
        Long parsedSubjectId;
        try {
            parsedSubjectId = Long.parseLong(subjectId);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid subjectId");
        }

        Subject subject = getSubjectById(parsedSubjectId);
        if (subject == null) {
            throw new Exception("Subject not found");
        }
        logger.info(mm + " .... searchVideosByTag, subject: " + subject.getTitle());
        return youTubeService.searchVideosByTag(maxResults.longValue(), subject, tagType);
    }
    private Subject getSubjectById(Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        return optionalSubject.orElse(null);
    }
}