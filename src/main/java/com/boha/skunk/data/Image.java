package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "exam_paper_id")
    private ExamPaper examPaper;

    @Column(name = "download_url", columnDefinition = "TEXT")
    String downloadUrl;

    @Column(name = "file_type")
    String fileType;

    public Image() {
    }


}
