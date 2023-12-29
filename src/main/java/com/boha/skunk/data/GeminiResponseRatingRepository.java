package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeminiResponseRatingRepository extends JpaRepository<GeminiResponseRating, Long> {

    List<GeminiResponseRating> findByExamPageImageId(Long examPageImageId);
}
