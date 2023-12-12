package com.boha.skunk.data;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exam_links")
public class ExamLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private ExamDocument examDocument;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "link")
    private String link;

    @Column(name = "title")
    private String title;

    @Column(name = "document_title")
    private String documentTitle;


    public ExamLink() {
    }
// Constructors, getters, and setters...

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title + " - " + link;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public Long getId() {
        return id;
    }

}
