package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class DataService {
    private final OKHelper okHelper;

    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 DataService \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(DataService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Value("${educUrl}")
    private String educUrl;

    private final ExamLinkService examLinkService;
    private final ExamPaperRepository examPaperRepository;
    private final ExamLinkRepository examLinkRepository;
    private final ImageRepository imageRepository;

    private final CloudStorageService cloudStorageService;

    private static final List<File> imageFiles = new ArrayList<>();

    public DataService(OKHelper okHelper, ExamLinkService examLinkService, ExamPaperRepository examPaperRepository, ExamLinkRepository examLinkRepository, ImageRepository imageRepository, CloudStorageService cloudStorageService) {
        this.okHelper = okHelper;
        this.examLinkService = examLinkService;
        this.examPaperRepository = examPaperRepository;
        this.examLinkRepository = examLinkRepository;
        this.imageRepository = imageRepository;
        this.cloudStorageService = cloudStorageService;
    }

    public List<Image> getExamImages(Long examPaperId) {
        List<Image> list = imageRepository.findByExamPaperId(examPaperId);
        List<Image> filtered = new ArrayList<>();
        for (Image image : list) {
            ExamPaper i = new ExamPaper();
            i.setId(image.getId());
            i.setDate(image.getExamPaper().getDate());
            i.setTitle(image.getExamPaper().getTitle());
            i.setExamLink(image.getExamPaper().getExamLink());
            image.setExamPaper(i);
            filtered.add(image);
        }
        return filtered;
    }
    public ExamPaper getExamPaper(Long examPaperId) {
        var opt =  examPaperRepository.findById(examPaperId);
        return opt.orElse(null);
    }
    private List<Image> extractImages(PdfReader reader, ExamPaper examPaper) throws IOException {
        File directory = createDirectoryIfNotExists("pdfs/images");
        logger.info(mm + " parse: ... starting ... ");
        imageFiles.clear();
        long size = 0;
        List<Image> images = new ArrayList<>();
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        logger.info(mm + " " + reader.getNumberOfPages() + " pages in pdf \uD83D\uDD35 parser: " + parser.toString());
        try {
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                SvgRenderListener listener = new SvgRenderListener(
                        directory.getPath() + "/img_" + System.currentTimeMillis() + ".svg");
                parser.processContent(i, listener);
            }

            for (File imageFile : imageFiles) {
                size += imageFile.length();
                var i = imageFile.getName().lastIndexOf(".");
                String fileType = "png";
                if (i > -1) {
                    fileType = imageFile.getName().substring(i + 1);
                }
                logger.info(mm + " image fileType: " + fileType);
                var url = cloudStorageService.uploadFile(imageFile);
                logger.info(mm + " before creating Image, url: " + url);

                Image image = new Image();
                image.setExamPaper(examPaper);
                image.setFileType(fileType);
                image.setDownloadUrl(url);
                var res = imageRepository.save(image);
                images.add(res);
                logger.info(mm + " image has been added to database for examPaper id: "
                        + image.getExamPaper().getId() + " url: " + res.getDownloadUrl());
            }

            logger.info(mm + " job complete! files created: " + imageFiles.size() + " size: " + (size / 1024) + "K");
            return images;
        } finally {
            //parser.close();
        }
    }

    public String cleanupString(String input) {
        // Use regular expression to replace multiple occurrences of \n with a single \n
        return input.replaceAll("\\s*\\n\\s*", "\n");
    }
    public ExamPaper extractExamPaper(Long examLinkId) throws Exception {
        OkHttpClient client = okHelper.getClient();
        logger.info(mm + " extractTextFromPdf starting ... examLinkId: " + examLinkId );
        var examLink = examLinkRepository.findById(examLinkId).orElse(null);
        if (examLink == null) {
            throw new Exception("ExamLink record not found");
        }
        logger.info(mm + " extractTextFromPdf starting ... examLinkId: "
                + examLinkId + " title: " + examLink.getTitle() );

        Request request = new Request.Builder()
                .url(examLink.getLink())
                .build();
        File directory = createDirectoryIfNotExists("pdfs");
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                try (InputStream inputStream = response.body().byteStream()) {
                    PdfReader reader = new PdfReader(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {

                        stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, i));
                    }
                    ExamPaper paper = new ExamPaper();
                    paper.setExamLink(examLink);
                    paper.setTitle(examLink.getTitle());
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    String isoString = dateFormat.format(currentDate);
                    paper.setDate(isoString);
                    String text = cleanupString(stringBuilder.toString().trim());
                    paper.setText(text);
                    // Save the extracted stringBuilder in a file
                    String outputFilePath = directory.getPath() + "/pdfText_" + System.currentTimeMillis() + ".txt";
                    try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                        outputStream.write(text.getBytes());
                        logger.info(mm + " Text file created:  \uD83D\uDD90\uD83C\uDFFE " + outputFilePath);
                    }

                    var mPaper = examPaperRepository.save(paper);
                    logger.info(mm+" ExamPaper written to the database: " +
                            "\uD83C\uDF4E \uD83C\uDF50\uD83C\uDF4E \uD83C\uDF50"
                            + mPaper.getTitle() + " examLinkId: " + mPaper.getExamLink().getId());
                    //
                    var list = extractImages(reader, mPaper);
                    reader.close();
                    logger.info(mm+" added " + list.size() + " \uD83D\uDD35 images to database for \uD83D\uDD35 " + mPaper.getTitle());
                    return paper;
                }
            } else {
                throw new IOException("Failed to download PDF file: " + response.code() + " - " + response.message());
            }
        }
    }

    private static File createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }


    private static class SvgRenderListener implements RenderListener {
        private final Writer writer;

        public SvgRenderListener(String outputFilePath) throws IOException {
            this.writer = new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8");
            writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100%\" height=\"100%\" viewBox=\"0 0 1000 1000\">");
        }

        @Override
        public void beginTextBlock() {
        }

        @Override
        public void renderText(TextRenderInfo textRenderInfo) {

        }

        @Override
        public void endTextBlock() {
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
            try {
                File directory = createDirectoryIfNotExists("pdfs/images");
                PdfImageObject imageObject = renderInfo.getImage();
                if (imageObject != null && (imageObject.getImageAsBytes().length > 2000)) {
                        logger.info(mm + "renderImage: \uD83D\uDD14 PdfImageObject.getFileType: " + imageObject.getFileType()
                                + " getImageAsBytes: " + imageObject.getImageAsBytes().length + " area: " + renderInfo.getArea());
                        byte[] imageData = imageObject.getImageAsBytes();
                        String fileType = imageObject.getFileType();
                        String outputFilePath = directory.getPath() + "/pdfImage_" + System.currentTimeMillis() + "." + fileType;
                        saveImageToFile(imageData, outputFilePath);
                        //
                        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
                        int width = bufferedImage.getWidth();
                        int height = bufferedImage.getHeight();
                        String base64Image = java.util.Base64.getEncoder().encodeToString(imageData);
                        Vector position = renderInfo.getStartPoint();
                        writer.write("<image x=\"" + position.get(Vector.I1) + "\" y=\"" + position.get(Vector.I2)
                                + "\" width=\"" + width + "\" height=\"" + height + "\" xlink:href=\"data:image/png;base64,"
                                + base64Image + "\"/>");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() throws IOException {
            writer.write("</svg>");
            writer.close();
        }

        private void saveImageToFile(byte[] imageData, String outputFilePath) throws IOException {
            try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                outputStream.write(imageData);
                logger.info(mm + " \uD83D\uDC9A saveImageToFile: " + outputFilePath + "  \uD83D\uDD90\uD83C\uDFFE");
                imageFiles.add(new File(outputFilePath));

            }
        }
    }
}