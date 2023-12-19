package com.boha.skunk.util;
import com.boha.skunk.data.ExamColumn;
import com.boha.skunk.data.ExamRow;
import com.boha.skunk.data.ExamTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExamTableRenderer extends TableRenderer {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 MyRenderer \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(ExamTableRenderer.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public ExamTableRenderer(Table modelElement) {
        super(modelElement);
    }

    @Override
    public void drawChildren(DrawContext drawContext) {
        super.draw(drawContext);
        logger.info(mm + "... draw in @Override: isTaggingEnabled: " +
                drawContext.isTaggingEnabled());
        Table table = (Table) getModelElement();
        int rows = table.getNumberOfRows();
        int columns = table.getNumberOfColumns();

        ExamTable myTable = new ExamTable();
        processTableHeader(table, myTable);

        List<ExamRow> myRows = new ArrayList<>();

        // Process the rows and columns
        for (int row = 0; row < rows; row++) {
            ExamRow myRow = new ExamRow();
            List<ExamColumn> myColumns = new ArrayList<>();
            for (int column = 0; column < columns; column++) {
                Cell cell = table.getCell(row, column);
                for (IElement child : cell.getChildren()) {
                    IRenderer childRenderer = child.getRenderer();
                    if (childRenderer instanceof TextRenderer textRenderer) {
                        String cellText = String.valueOf(textRenderer.getText());
                        // Handle the cell data as needed
                        logger.info(mm + " row: " + row + " column: " + column +
                                " Cell Text: " + cellText);
                        ExamColumn myColumn = new ExamColumn();
                        myColumn.setText(cellText);
                        myColumns.add(myColumn);
                    }
                }
            }
            myRow.setColumns(myColumns);
            myRows.add(myRow);
        }

        myTable.setRows(myRows);
        logger.info(mm+" Table created: " + G.toJson(rows));
        // You can now access the populated myTable object outside the draw method
    }


    private void processTableHeader(Table table, ExamTable examTable) {
        Table header = table.getHeader();
        examTable.setRows(new ArrayList<>());
        if (header != null) {
            int headerColumns = header.getNumberOfColumns();
            ExamRow headerRow = new ExamRow();
            List<ExamColumn> headerColumnsList = new ArrayList<>();
            for (int column = 0; column < headerColumns; column++) {
                Cell cell = header.getCell(0, column);
                for (IElement child : cell.getChildren()) {
                    IRenderer childRenderer = child.getRenderer();
                    if (childRenderer instanceof TextRenderer textRenderer) {
                        String cellText = String.valueOf(textRenderer.getText());
                        ExamColumn headerColumn = new ExamColumn();
                        headerColumn.setText(cellText);
                        headerColumnsList.add(headerColumn);
                    }
                }
            }
            headerRow.setColumns(headerColumnsList);
            examTable.getRows().add(0, headerRow);
        }
    }


}