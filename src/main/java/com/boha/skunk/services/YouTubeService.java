package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class YouTubeService {

    private static final String APPLICATION_NAME = "Skunk Video Search";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D YouTubeService \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(YouTubeService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private final YouTube youTube;

    @Value("${API_KEY}")
    private String apiKey;

    public YouTubeService() throws GeneralSecurityException, IOException {


        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.youTube = new YouTube.Builder(httpTransport, JSON_FACTORY, getRequestInitializer())
                .setApplicationName(APPLICATION_NAME)
                .build();
        logger.info(mm + "YouTube initialized");
    }

    private HttpRequestInitializer getRequestInitializer() {
        try {
            GoogleCredential credential = GoogleCredential.getApplicationDefault()
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/youtube"));

            // Set the API key in the GoogleCredential object
            credential = credential.createDelegated(apiKey);
            logger.info(mm + "YouTube initialized: access scopes: "
                    + credential.getServiceAccountScopes());
            return credential;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<YouTubeData> searchChannels(String query, Long maxResults, Subject subject) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("channel");
        return search(query, list, maxResults, subject);
    }

    public List<YouTubeData> searchPlaylists(String query, Long maxResults, Subject subject) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("playlist");
        return search(query, list, maxResults, subject);
    }

    public List<YouTubeData> searchVideosByTag(
            Long maxResults, Subject subject, int tagType) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("video");
//        List<Tag> tags = tagRepository.findBySubjectIdAndTagType(subject.getId(), tagType);
//        StringBuilder sb = new StringBuilder();
//        sb.append(subject.getTitle()).append(" ");
//        for (Tag tag : tags) {
//            if (tag.getTagType() == tagType) {
//                sb.append(tag.getText()).append(" ");
//            }
//        }
        logger.info(mm + "query string: " + subject.getTitle());
        var results = search(subject.getTitle(), list, maxResults, subject);
        logger.info(mm+" video search found: " + results.size() + " YouTube videos\n\n");
        return results;
    }

    public List<YouTubeData> searchVideos(String query, Long maxResults, Subject subject) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("video");
        return search(query, list, maxResults, subject);
    }

    private List<YouTubeData> search(String query,
                                     List<String> types,
                                     Long maxResults,
                                     Subject subject) throws IOException {
        logger.info(mm + "subject: " + subject.getTitle());
        logger.info(mm + "query: " + query);
        logger.info(mm + "types: " + types.toString());
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("snippet");
        YouTube.Search.List searchRequest = youTube.search().list(list);
        searchRequest.setQ(subject.getTitle() + " - " + query);
        searchRequest.setType(types);
        searchRequest.setMaxResults(maxResults);
        List<YouTubeData> youTubeDataList = new ArrayList<>();

        SearchListResponse searchResponse = searchRequest.execute();
        for (SearchResult searchResult : searchResponse.getItems()) {
            String videoId = searchResult.getId().getVideoId();
            String channelId = searchResult.getSnippet().getChannelId();
            String title = searchResult.getSnippet().getTitle();
            String description = searchResult.getSnippet().getDescription();
            String channelTitle = searchResult.getSnippet().getChannelTitle();
            String playlistId = null;
            if (searchResult.getId().getPlaylistId() != null) {
                playlistId = searchResult.getId().getPlaylistId();

            }
            print(searchResult, videoId, channelId, title, description, channelTitle, playlistId);
            String high = searchResult.getSnippet().getThumbnails().getHigh().getUrl();
            String med = searchResult.getSnippet().getThumbnails().getMedium().getUrl();
            String def = searchResult.getSnippet().getThumbnails().getDefault().getUrl();

            YouTubeData youTubeData = new YouTubeData(channelId, channelTitle, videoId,
                    playlistId, title, description, high, med, def, subject);

            youTubeDataList.add(youTubeData);

        }
        logger.info(mm + "\n");
        logger.info(mm + " Total Search responses: \uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E "
                + youTubeDataList.size() + "\n\n");

        return youTubeDataList;
    }

    private static void print(SearchResult searchResult, String videoId,
                              String channelId, String title, String description,
                              String channelTitle, String playlistId) {
        // Print or use the video information
        logger.info("\n"+mm + "Video ID: \uD83C\uDF4E " + videoId + " Title: \uD83C\uDF4E " + title);
        logger.info(mm + "Description: \uD83D\uDD35 " + description);
        logger.info(mm + "ChannelId: " + channelId + " \uD83D\uDD90\uD83C\uDFFD Title: \uD83D\uDD35 " + channelTitle);
        logger.info(mm + " Go to Youtube Video: \uD83D\uDD90\uD83C\uDFFD https://www.youtube.com/watch?v=" + videoId);
        logger.info(mm + " Go to channel:\uD83D\uDD90\uD83C\uDFFD  https://www.youtube.com/channel/" + channelId);
        if (playlistId != null) {
            logger.info(mm + " Go to playlist: https://www.youtube.com/playlist?list="
                    + playlistId + "\n\n");
        }
        logger.info(mm + "thumbnail(high): " +
                searchResult.getSnippet().getThumbnails().getHigh().getUrl());
        logger.info(mm + "thumbnail(medium): " +
                searchResult.getSnippet().getThumbnails().getMedium().getUrl());
        logger.info(mm + "thumbnail(default): " +
                searchResult.getSnippet().getThumbnails().getDefault().getUrl());

    }

}
