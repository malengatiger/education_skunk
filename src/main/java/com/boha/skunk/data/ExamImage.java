package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_images")
public class ExamImage {
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

    @Column(name = "index")
    int index;

    public ExamImage() {
    }

    public Long getId() {
        return id;
    }


    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
