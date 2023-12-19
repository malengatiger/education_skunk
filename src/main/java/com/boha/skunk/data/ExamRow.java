package com.boha.skunk.data;

import lombok.Data;

import java.util.List;
public class ExamRow {
    List<ExamColumn> columns;

    public List<ExamColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ExamColumn> columns) {
        this.columns = columns;
    }
}
