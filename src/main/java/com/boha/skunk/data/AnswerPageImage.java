package com.boha.skunk.data;



//@Table(name = "answer_page_images")
public class AnswerPageImage {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //
    //(name = "exam_document_id")
    private ExamDocument examDocument;

   //@Column(name = "download_url", columnDefinition = "TEXT")
    String downloadUrl;

   //@Column(name = "page_index")
    int pageIndex;
   //@Column(name = "date")
    String date;
   //@Column(name = "mime_type")
    String mimeType;
    public AnswerPageImage() {
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
