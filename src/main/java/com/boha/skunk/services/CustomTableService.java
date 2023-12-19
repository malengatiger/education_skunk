package com.boha.skunk.services;

import com.boha.skunk.data.ExamTable;
import com.boha.skunk.util.Downloader;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomTableService {


    public List<ExamTable> createExamTables(String pdfUrl) throws Exception {
        File file = Downloader.downloadPdf(pdfUrl);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(file));
//        CustomTableRenderer renderer = new CustomTableRenderer();
//        PdfCanvasProcessor processor = new PdfCanvasProcessor((IEventListener) renderer);
//
//
//        List<ExamTable> examTables = new ArrayList<>();
//
//        int pages = pdfDoc.getNumberOfPages();
//
//
//        for (int pageNum = 1; pageNum <= pages; pageNum++) {
//            PdfPage page = pdfDoc.getPage(pageNum);
//            processor.processPageContent(page);
//        }

        pdfDoc.close();
        return new ArrayList<>();
    }
}

