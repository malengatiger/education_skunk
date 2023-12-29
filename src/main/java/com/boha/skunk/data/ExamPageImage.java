package com.boha.skunk.data;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_page_images")
public class ExamPageImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "exam_link_id")
    private ExamLink examLink;

    @Column(name = "download_url", columnDefinition = "TEXT")
    String downloadUrl;

    @Column(name = "page_index")
    int pageIndex;
    @Column(name = "date")
    String date;
    @Column(name = "mime_type")
    String mimeType;
    public ExamPageImage() {
    }

    public Long getId() {
        return id;
    }


    public ExamLink getExamLink() {
        return examLink;
    }

    public void setExamLink(ExamLink examLink) {
        this.examLink = examLink;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
