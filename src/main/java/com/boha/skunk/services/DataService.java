package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
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
    private final ImageRepository imageRepository;
    private final ExamTableRepository examTableRepository;

    private final CloudStorageService cloudStorageService;
    private final TableExtractorService tableExtractorService;
    private static final List<File> imageFiles = new ArrayList<>();

    public ExamPaper extractExamPaper(Long examLinkId) throws Exception {
        OkHttpClient client = okHelper.getClient();
        logger.info(mm + " extractTextFromPdf starting ... examLinkId: " + examLinkId);
        var examLink = examLinkRepository.findById(examLinkId).orElse(null);
        if (examLink == null) {
            throw new Exception("ExamLink record not found");
        }
        var examPaper = examPaperRepository.findByExamLinkId(examLinkId);
        if (examPaper != null) {
            return examPaper;
        }
        logger.info(mm + " extractTextFromPdf starting ... examLinkId: "
                + examLinkId + " \uD83D\uDC9C title: " + examLink.getTitle());

        Request request = new Request.Builder()
                .url(examLink.getLink())
                .build();
        File directory = createDirectoryIfNotExists("pdfs");
        ExamPaper paper = new ExamPaper();
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
                    PdfReader reader = new PdfReader(outputFile.getAbsolutePath());
                    StringBuilder stringBuilder = new StringBuilder();
                    PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(
                            new File(directory.getPath() + "/file_out_"
                                    + date + ".pdf")));

                    for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                        PdfPage pdfPage = pdfDoc.getPage(i);
                        stringBuilder.append(PdfTextExtractor.getTextFromPage(pdfPage));
                        stringBuilder.append("\n");
                    }

                    paper.setExamLink(examLink);
                    paper.setTitle(examLink.getTitle());
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    String isoString = dateFormat.format(currentDate);
                    paper.setDate(isoString);
                    String text = cleanupString(stringBuilder.toString().trim());
                    paper.setText(text);
                    // Save the extracted stringBuilder in a file
                    String outputFilePath = directory.getPath() + "/pdfText_" +
                            date + ".txt";
                    try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                        outputStream.write(text.getBytes());
                        logger.info(mm + " Text file created:  \uD83D\uDD90\uD83C\uDFFE "
                                + outputFilePath);
                    }

                    //
                    var mPaper = examPaperRepository.save(paper);
                    logger.info(mm + " ExamPaper written to the database: " +
                            "\uD83C\uDF4E \uD83C\uDF50\uD83C\uDF4E \uD83C\uDF50"
                            + mPaper.getTitle() + " examLinkId: " + mPaper.getExamLink().getId()
                            + " examPaperId: " + mPaper.getId());

                    //
                    reader.close();
                }
            } else {
                throw new IOException("Failed to download PDF file: " + response.code() + " - " + response.message());
            }
        }
        return paper;
    }


    public ExamPaper getExamPaper(Long examPaperId) {
        var opt = examPaperRepository.findById(examPaperId);
        return opt.orElse(null);
    }



    public String cleanupString(String input) {
        // Use regular expression to replace multiple occurrences of \n with a single \n
        return input.replaceAll("\\s*\\n\\s*", "\n");
    }

    private static File createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }


    public static class CustomEventListener implements IEventListener {
        //        private static void saveImageToFile(byte[] imageData, String outputFilePath) throws IOException {
//            try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
//                outputStream.write(imageData);
//                logger.info(mm + " \uD83D\uDC9A saveImageToFile: " + outputFilePath + "  \uD83D\uDD90\uD83C\uDFFE");
//                imageFiles.add(new File(outputFilePath));
//
//            }
//        }
        public boolean isValidImage(byte[] imageData) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
                // Attempt to read the image using ImageIO
                ImageIO.read(inputStream);
                logger.info(mm + "Image is valid: ");
                return true;
            } catch (IOException e) {
                logger.info(mm + "Image is NOT valid: ");
                return false;
            }
        }

        //Bard - Google Gemini API - AI - AIzaSyAuArZYG0wNXtNdz8aa1YXCjYxlVcnDF8M
        @Override
        public void eventOccurred(IEventData data, EventType type) {
            // Handle the event based on the event type
            if (type == EventType.RENDER_TEXT) {
                // Handle text rendering event
                TextRenderInfo renderInfo = (TextRenderInfo) data;
                String text = renderInfo.getText();
                // Process the text as needed
            } else if (type == EventType.RENDER_IMAGE) {
                // Handle image rendering event
                if (data instanceof ImageRenderInfo renderInfo) {
                    try {
                        File directory = createDirectoryIfNotExists("pdfs/images");
                        PdfImageXObject imageObject = renderInfo.getImage();
                        if (imageObject != null && (imageObject.getImageBytes().length > 2048)) {
                            byte[] imageData = imageObject.getImageBytes();
                            if (isValidImage(imageData)) {
                                String fileType = imageObject.identifyImageFileExtension();
                                if (fileType == null) {
                                    fileType = "png"; // Default to PNG if the file type is not identified
                                }
                                logger.info(mm + "File type found: " + fileType);
                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//                            String date = sdf.format(new Date());
                                String outputFilePath = directory.getPath() + "/pdfImage_" + System.currentTimeMillis() + "." + fileType;

                                try (OutputStream outputStream = new FileOutputStream(outputFilePath)) {
                                    outputStream.write(imageData);
                                }

                                imageFiles.add(new File(outputFilePath));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Handle other event types if necessary
        }


        @Override
        public Set<EventType> getSupportedEvents() {
            // Return the set of event types supported by this listener
            return EnumSet.of(EventType.RENDER_TEXT, EventType.RENDER_IMAGE);
        }
    }
}
