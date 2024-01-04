package com.boha.skunk.data;

import lombok.Data;



public class Tag {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "subject_id")
    private Subject subject;

   //@Column(name = "text")
    private String text;

   //@Column(name = "tag_type")
    private int tagType;

    public Tag() {
    }

    public Tag(Subject subject, String text, int tagType) {
        this.subject = subject;
        this.text = text;
        this.tagType = tagType;
    }

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}