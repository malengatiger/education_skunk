package com.boha.skunk.data;



//@Table(name = "gemini_response_rating")
public class GeminiResponseRating {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

   //@Column(name = "page_number")
    int pageNumber;
   //@Column(name = "exam_link_id")
    Long examLinkId;
   //@Column(name = "rating")
    int rating;
    int tokensUsed;
   //@Column(name = "date")
    String date;
   //@Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText;
   //@Column(name = "prompt", columnDefinition = "TEXT")
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


    public int getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(int tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getExamLinkId() {
        return examLinkId;
    }

    public void setExamLinkId(Long examLinkId) {
        this.examLinkId = examLinkId;
    }

    public void setId(Long id) {
        this.id = id;
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
