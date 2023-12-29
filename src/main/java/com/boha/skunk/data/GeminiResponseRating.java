package com.boha.skunk.data;

import jakarta.persistence.*;

@Entity
@Table(name = "gemini_response_rating")
public class GeminiResponseRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "exam_page_image_id")
    private Long examPageImageId;

    @Column(name = "rating")
    int rating;
    @Column(name = "date")
    String date;
    @Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText;
    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt;
     public GeminiResponseRating() {
    }

    public Long getId() {
        return id;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }



    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Long getExamPageImageId() {
        return examPageImageId;
    }

    public void setExamPageImageId(Long examPageImageId) {
        this.examPageImageId = examPageImageId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
