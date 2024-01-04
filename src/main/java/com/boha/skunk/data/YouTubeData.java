package com.boha.skunk.data;


public class YouTubeData {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
   //@Column(name = "title")
    String title;
   //@Column(name = "description", columnDefinition = "TEXT")
    String description;
   //@Column(name = "channel_id")
    String channelId;
   //@Column(name = "channel_title")
    String channelTitle;
   //@Column(name = "video_id")
    String videoId;
   //@Column(name = "playlist_id")
    String playlistId;
   //@Column(name = "video_url", columnDefinition = "TEXT")
    String videoUrl;
   //@Column(name = "channel_url", columnDefinition = "TEXT")
    String channelUrl;
   //@Column(name = "playlist_url", columnDefinition = "TEXT")
    String playlistUrl;

   //@Column(name = "thumbnailHigh", columnDefinition = "TEXT")
    String thumbnailHigh;
   //@Column(name = "thumbnailMedium", columnDefinition = "TEXT")
    String thumbnailMedium;

   //@Column(name = "thumbnailDefault", columnDefinition = "TEXT")
    String thumbnailDefault;

    //
    //(name = "subject_id")
    Subject subject;

    static final String VIDEO = "https://www.youtube.com/watch?v=";
    static final String CHANNEL = "https://www.youtube.com/channel/";
    static final String PLAYLIST = "https://www.youtube.com/playlist?list=";

    public YouTubeData() {
    }

    public Long getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl() {
        this.videoUrl = VIDEO + videoId;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl() {
        this.channelUrl = CHANNEL + channelId;
    }

    public String getPlaylistUrl() {
        return playlistUrl;
    }

    public void setPlaylistUrl() {
        this.playlistUrl = PLAYLIST + playlistUrl;
    }

    public String getThumbnailHigh() {
        return thumbnailHigh;
    }

    public void setThumbnailHigh(String thumbnailHigh) {
        this.thumbnailHigh = thumbnailHigh;
    }

    public String getThumbnailMedium() {
        return thumbnailMedium;
    }

    public void setThumbnailMedium(String thumbnailMedium) {
        this.thumbnailMedium = thumbnailMedium;
    }

    public String getThumbnailDefault() {
        return thumbnailDefault;
    }

    public void setThumbnailDefault(String thumbnailDefault) {
        this.thumbnailDefault = thumbnailDefault;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public YouTubeData(String channelId, String channelTitle,
                       String videoId, String playlistId,
                       String title, String description,
                       String thumbnailHigh, String thumbnailMedium,
                       String thumbnailDefault, Subject subject) {
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.videoId = videoId;
        this.playlistId = playlistId;
        this.title = title;
        this.description = description;

        this.thumbnailHigh = thumbnailHigh;
        this.thumbnailMedium = thumbnailMedium;
        this.thumbnailDefault = thumbnailDefault;
        this.subject = subject;

        if (playlistId != null) {
            this.playlistUrl = PLAYLIST + playlistId;
        }
        if (videoId != null) {
            this.videoUrl = VIDEO + videoId;
        }
        if (channelId != null) {
            this.channelUrl = CHANNEL + channelId;
        }

    }
}
