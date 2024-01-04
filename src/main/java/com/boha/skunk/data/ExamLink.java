package com.boha.skunk.data;



import java.util.List;


//@Table(name = "exam_links")
public class ExamLink {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "document_id")
    private ExamDocument examDocument;

    //
    //(name = "subject_id")
    private Subject subject;


   //@Column(name = "exam_text", columnDefinition = "TEXT")

    private String examText;

   //@Column(name = "link", columnDefinition = "TEXT")
    private String link;

   //@Column(name = "title")
    private String title;

   //@Column(name = "document_title")
    private String documentTitle;

   //@Column(name = "page_image_zip_url", columnDefinition = "TEXT")
    private String pageImageZipUrl;

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamText() {
        return examText;
    }

    public void setExamText(String examText) {
        this.examText = examText;
    }

    public ExamLink() {
    }
// Constructors, getters, and setters...

    public String getPageImageZipUrl() {
        return pageImageZipUrl;
    }

    public void setPageImageZipUrl(String pageImageZipUrl) {
        this.pageImageZipUrl = pageImageZipUrl;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public Long getId() {
        return id;
    }

    public ExamDocument getExamDocument() {
        return examDocument;
    }

    public void setExamDocument(ExamDocument examDocument) {
        this.examDocument = examDocument;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
}
