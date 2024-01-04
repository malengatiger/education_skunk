package com.boha.skunk.data;



//@Table(name = "exam_documents")

public class ExamDocument {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

   //@Column(name = "title")
    String title;

   //@Column(name = "link", columnDefinition = "TEXT")
    String link;

    public ExamDocument() {
    }

    public ExamDocument(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
