package com.boha.skunk.data;

import com.boha.skunk.util.ExamRowConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "exam_tables")
public class ExamTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "rows", columnDefinition = "TEXT")
    @Convert(converter = ExamRowConverter.class)
    List<ExamRow> rows;


    @ManyToOne
    @JoinColumn(name = "exam_paper_id")
    ExamPaper examPaper;

    @Column(name = "index")
    int index;

    public Long getId() {
        return id;
    }

    public List<ExamRow> getRows() {
        return rows;
    }

    public void setRows(List<ExamRow> rows) {
        this.rows = rows;
    }

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
