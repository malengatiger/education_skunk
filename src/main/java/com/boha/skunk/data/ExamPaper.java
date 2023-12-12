package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exam_papers")
public class ExamPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamLink examLink;

    @Column(name = "title")
    private String title;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "date")
    private String date;

    public ExamPaper() {
    }


}