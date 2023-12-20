package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeDataRepository extends JpaRepository<YouTubeData, Long> {

  YouTubeData findByChannelIdAndVideoId(String channelId, String videoId);
}
