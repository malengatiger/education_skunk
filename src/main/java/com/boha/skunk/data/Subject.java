package com.boha.skunk.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "subjects")

public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "subject")
    List<ExamLink> examLinks;

    @OneToMany(mappedBy = "subject")
    List<YouTubeData> youTubeData;

    @Column(name = "title", unique = true) // Add unique constraint
    String title;


    public Subject() {
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
