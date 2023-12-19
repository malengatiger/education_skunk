package com.boha.skunk.services;

import com.boha.skunk.data.ExamTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.PathRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.TableRenderer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ExamTableService {
    static final String mm = " \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E " +
            "ExamTableService  \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(ExamTableService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public List<ExamTable> parseTableData(File examPdfFile) throws IOException {
        logger.info(mm + " parseTableData ....");

        com.itextpdf.kernel.pdf.PdfReader reader = new com.itextpdf.kernel.pdf.PdfReader(examPdfFile.getAbsolutePath());
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(reader);
//        Document document = new Document(pdfDoc);

        List<ExamTable> examTables = new ArrayList<>();
        logger.info(mm + "  ... processing pages: " + pdfDoc.getNumberOfPages());

        try {
            for (int pageNum = 1; pageNum <= pdfDoc.getNumberOfPages(); pageNum++) {
                PdfPage page = pdfDoc.getPage(pageNum);

                IEventListener eventListener = new IEventListener() {
                    @Override
                    public void eventOccurred(IEventData data, EventType type) {
                        logger.info(mm+"eventOccurred: " + type.name() + " - " + data.toString());
                        if ( type == EventType.RENDER_IMAGE
                                || type == EventType.RENDER_TEXT) {
                            if (data instanceof TextRenderInfo) {
                                logger.info(mm+"Handle text rendering");
                            } else if (data instanceof PathRenderInfo) {
                                // Handle path rendering
                                logger.info(mm+"Handle text rendering");
                            } else if (data instanceof ImageRenderInfo) {
                                // Handle image rendering
                                logger.info(mm+"Handle image rendering");
                            }
                        } else if (type == EventType.RENDER_PATH) {
                            if (data instanceof TableRenderer) {
                                logger.info(mm+"this is a TableRenderer");
                            }
                        }
                    }

                    @Override
                    public Set<EventType> getSupportedEvents() {
                        return EnumSet.of(EventType.RENDER_TEXT, EventType.RENDER_IMAGE, EventType.RENDER_PATH);
                    }
                };

                PdfCanvasProcessor processor = new PdfCanvasProcessor(eventListener);
                processor.processPageContent(page);

    //            if (tableRenderer != null) {
    //                Table table = tableRenderer.getModelElement();
    //                ExamTable examTable = extractDataFromTable(table);
    //                examTables.add(examTable);
    //            }
            }

            pdfDoc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return examTables;
    }





    private ExamTable extractDataFromTable(Table table) {
        ExamTable examTable = new ExamTable();

        // Extract data from the table and populate the examTable object
        // ...

        return examTable;
    }
    private boolean isExamTable(Table table) {
        // Implement logic to identify and filter based on specific characteristics of exam tables
        // (e.g., header row, question structure, answer choices format)

        // Replace this with your specific criteria for detecting exam tables
        logger.info(mm+" rows: " + table.getNumberOfRows() + " cols: " + table.getNumberOfColumns());
        return table.getNumberOfRows() > 0 && table.getNumberOfColumns() > 1;
    }
}

