package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exam_documents")

public class ExamDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "link")
    String link;

    public ExamDocument() {
    }

    public ExamDocument(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
