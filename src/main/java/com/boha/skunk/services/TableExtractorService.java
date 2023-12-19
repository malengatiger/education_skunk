package com.boha.skunk.services;


import com.boha.skunk.data.ExamPaper;
import com.boha.skunk.data.ExamPaperRepository;
import com.boha.skunk.data.ExamTable;
import com.boha.skunk.data.ExamTableRepository;
import com.boha.skunk.util.Downloader;
import com.boha.skunk.util.ExamTableRenderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.boha.skunk.util.Downloader.downloadPdf;


@Service
@RequiredArgsConstructor
public class TableExtractorService {

    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
            "TableExtractorService \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(ExamTableRenderer.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private final ExamTableRepository examTableRepository;

    private final ExamPaperRepository examPaperRepository;
    private final ExamTableService examTableService;


    public List<ExamTable> extractExamTables(Long examPaperId, String url) throws IOException {
        File pdfFile = downloadPdf(url);
        Optional<ExamPaper> paperOpt = examPaperRepository.findById(examPaperId);
        ExamPaper examPaper;
        if (paperOpt.isPresent()) {
            examPaper = paperOpt.get();
        } else {
           throw new RuntimeException("ExamPaper not found");
        }
        List<ExamTable> examTables = new ArrayList<>();
        logger.info(mm+"... ExamPaper: " + examPaper.getId()
        + " title: " + examPaper.getTitle());
        File outFile = new File(Downloader.createDirectoryIfNotExists().getPath() + "/html_" + System.currentTimeMillis() + ".html");
        logger.info(mm + " pdf file for tables: " + pdfFile.length() + " - " + pdfFile.getPath());


        try {
            PDDocument pdf = Loader.loadPDF(pdfFile);
            Writer output = new PrintWriter(outFile, StandardCharsets.UTF_8);
            PDFDomTree parser = new PDFDomTree();
            ByteArrayOutputStream bass = new ByteArrayOutputStream();
            Writer output2 = new PrintWriter(bass, true, StandardCharsets.UTF_8);
            parser.writeText(pdf, output2);
//            new PDFDomTree().writeText(pdf, output);
            logger.info(mm + " html file created: " +
                    outFile.length() + " - " + outFile.getPath());
            output.close();
            // Read the outFile as a String
            String myHtml = Files.readString(outFile.toPath());

            // Pass the myHtml string to Jsoup for further processing
            Document document = Jsoup.parse(myHtml);
            // Find all tables in the document
            Elements tables = document.select("table");
            for (Element table : tables) {
                Elements elements = table.getAllElements();
                logger.info(mm+" table elements: "+elements.size());
                int cnt = 0;
                for (Element element : elements) {
                    logger.info(mm+" table element: #" + cnt + " "+element.text());
                    cnt++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return examTables;
    }

}

