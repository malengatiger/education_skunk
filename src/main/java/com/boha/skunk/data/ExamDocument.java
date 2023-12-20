package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_documents")

public class ExamDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "link", columnDefinition = "TEXT")
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
}
