package com.boha.skunk.util;

import com.boha.skunk.data.ExamColumn;
import com.boha.skunk.data.ExamRow;
import com.boha.skunk.data.ExamTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.logging.Logger;

public class TablePrinter {
    private static final Logger logger = Logger.getLogger(TablePrinter.class.getSimpleName());
    static final String mm = "\uD83D\uDD36 \uD83D\uDD37 \uD83D\uDD36 \uD83D\uDD37 " +
            "TablePrinter : ";
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public static String getTableString(List<ExamTable> tables) {
        StringBuilder sb = new StringBuilder();
        for (ExamTable table : tables) {
            sb.append("Table ID: ").append(table.getId()).append("\n");
            List<ExamRow> rows = table.getRows();
            if (rows.isEmpty()) {
                sb.append("Empty table\n");
                continue;
            }

            int numColumns = rows.get(0).getColumns().size();

            sb.append(getHorizontalLine(numColumns));
            sb.append(getRowString(table.getRows().get(0)));
            sb.append(getHorizontalLine(numColumns));

            for (int i = 1; i < rows.size(); i++) {
                sb.append(getRowString(rows.get(i)));
            }

            sb.append(getHorizontalLine(numColumns));
            sb.append("\n");
        }
        logger.info(mm+" exam tables: " + tables.size() + " string: "
                + sb.toString());
        logger.info(mm+" Exam Tables: " + G.toJson(tables));
        return sb.toString();
    }

    private static String getRowString(ExamRow row) {
        StringBuilder sb = new StringBuilder();
        List<ExamColumn> columns = row.getColumns();
        sb.append("|");
        for (ExamColumn column : columns) {
            sb.append(" ").append(column.getText()).append(" |");
        }
        sb.append("\n");
        return sb.toString();
    }

    private static String getHorizontalLine(int numColumns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numColumns; i++) {
            sb.append("+---");
        }
        sb.append("+\n");
        return sb.toString();
    }
}
