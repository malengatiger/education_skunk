package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DataService {
    private final OKHelper okHelper;

    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 DataService \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(DataService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Value("${educUrl}")
    private String educUrl;

    private final ExamPaperRepository examPaperRepository;
    private final ExamLinkRepository examLinkRepository;

    private final CloudStorageService cloudStorageService;
    private String extractSentences(String text) {
        List<String> sentences = new ArrayList<>();

        // Split the text into sentences using a regular expression
        String[] sentenceArray = text.split("[.!?]");

        // Trim and add each sentence to the list
        for (String sentence : sentenceArray) {
            String trimmedSentence = sentence.trim();
            if (!trimmedSentence.isEmpty()) {
                sentences.add(trimmedSentence);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String sentence : sentences) {
            sb.append(sentence).append("\n");
        }
        logger.info(mm+"Sentences extracted, text is \n"+ sb.toString());
        return sb.toString();
    }
    public String extractExamPaperText(Long examLinkId) throws Exception {
        OkHttpClient client = okHelper.getClient();
        logger.info(mm + " .... extractTextFromPdf starting ... link: " + examLinkId);
        ExamLink examLink = examLinkRepository.findById(examLinkId).orElse(null);
        if (examLink == null) {
            throw new Exception("ExamLink record not found");
        }


        Request request = new Request.Builder()
                .url(examLink.getLink())
                .build();
        File directory = createDirectoryIfNotExists();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String date = sdf.format(new Date());

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                try (InputStream inputStream = response.body().byteStream()) {
                    File outputFile = new File(directory.getPath()
                            + "/exam_file_downloaded_" + date + ".pdf");
                    try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    logger.info(mm+" file downloaded: " + (outputFile.length()/1024) + "K bytes");
                    PdfReader reader = new PdfReader(outputFile.getAbsolutePath());
                    StringBuilder stringBuilder = new StringBuilder();
                    PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(
                            new File(directory.getPath() + "/file_out_"
                                    + date + ".pdf")));

                    logger.info(mm+" pdf file has: " + pdfDoc.getNumberOfPages() + " pages");

                    for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                        PdfPage pdfPage = pdfDoc.getPage(i);
                        stringBuilder.append(PdfTextExtractor.getTextFromPage(pdfPage));
                        stringBuilder.append("\n");
                    }

                    String text = cleanupString(stringBuilder.toString().trim());
                    //
                    examLink.setExamText(text);
                    var mText = examLinkRepository.save(examLink);
                    logger.info(mm + " ExamLink written to the database with text: " +
                            "\uD83C\uDF4E \uD83C\uDF50\uD83C\uDF4E \uD83C\uDF50"
                            + mText.getId()
                            + " title: " + examLink.getTitle()
                            + " text length: " + text.length() + " bytes");

                    //
                    var ok = outputFile.delete();
                    logger.info(mm+" pdf file deleted: " + ok);
                    reader.close();
                    return mText.getExamText();
                }
            } else {
                throw new IOException("Failed to download PDF file: " + response.code() + " - " + response.message());
            }
        }
    }


    public String cleanupString(String input) {
        // Use regular expression to replace multiple occurrences of \n with a single \n
        return input.replaceAll("\\s*\\n\\s*", "\n");
    }

    private static File createDirectoryIfNotExists() throws IOException {
        Path path = Paths.get("pdfs");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }


}
