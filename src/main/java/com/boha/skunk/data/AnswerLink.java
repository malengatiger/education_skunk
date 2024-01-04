package com.boha.skunk.data;




//@Table(name = "answer_links")
public class AnswerLink {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "document_id")
    private ExamDocument examDocument;

   //@Column(name = "answer_text", columnDefinition = "TEXT")

    private String answerText;

   //@Column(name = "link", columnDefinition = "TEXT")
    private String link;

   //@Column(name = "title")
    private String title;

   //@Column(name = "document_title")
    private String documentTitle;

   //@Column(name = "page_image_zip_url", columnDefinition = "TEXT")
    private String pageImageZipUrl;


    public AnswerLink() {
    }
// Constructors, getters, and setters...


    public Long getId() {
        return id;
    }


    public ExamDocument getExamDocument() {
        return examDocument;
    }

    public void setExamDocument(ExamDocument examDocument) {
        this.examDocument = examDocument;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getPageImageZipUrl() {
        return pageImageZipUrl;
    }

    public void setPageImageZipUrl(String pageImageZipUrl) {
        this.pageImageZipUrl = pageImageZipUrl;
    }
}
