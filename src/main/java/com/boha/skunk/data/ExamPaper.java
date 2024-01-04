package com.boha.skunk.data;



//@Table(name = "exam_papers")
public class ExamPaper {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "exam_id")
    private ExamLink examLink;

   //@Column(name = "title")
    private String title;

   //@Column(name = "text", columnDefinition = "TEXT")
    private String text;

   //@Column(name = "date")
    private String date;


    public ExamPaper() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}