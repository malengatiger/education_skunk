package com.boha.skunk.data;

import lombok.Data;

import java.util.List;


//@Table(name = "subjects")

public class Subject {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //@Table(mappedBy = "subject")
    List<ExamLink> examLinks;

    //@Table(mappedBy = "subject")
    List<YouTubeData> youTubeData;

   //@Column(name = "title", unique = true) // Add unique constraint
    String title;


    public Subject() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject(String title) {
        this.title = title;
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
}
