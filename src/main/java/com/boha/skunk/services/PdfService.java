package com.boha.skunk.services;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.data.ExamLinkRepository;
import com.boha.skunk.util.DirectoryUtils;
import com.boha.skunk.util.TrustAllCertificates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 PdfService \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(PdfService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    private final ExamLinkRepository examLinkRepository;
    private final CloudStorageService cloudStorageService;
    public File getPdfPageImages(Long examLinkId) throws Exception {
        logger.info(mm+" get pdf page images for examLink: " + examLinkId);
        disableCerts();
        Optional<ExamLink> optionalPaper = examLinkRepository.findById(examLinkId);
        ExamLink examLink = optionalPaper.orElse(null); // Provide a default value if the optional is empty
        if (examLink != null) {
            if (examLink.getPageImageZipUrl() != null) {
                return cloudStorageService.downloadFile(examLink.getPageImageZipUrl());
            }
            logger.info(mm+" get pdf file from examLink: " + examLink.getTitle());
            try {
                File dir = DirectoryUtils.createDirectoryIfNotExists(
                        "pdf_page_images/exam_"+examLinkId);
                URL url = new URL(examLink.getLink());
                File pdfFile = new File(dir.getPath()+"/pdf_"+examLinkId+".pdf");
                FileUtils.copyURLToFile(url, pdfFile);
                List<File> files = convertPdfToImages(pdfFile, dir);
                File zip = createZipFile(files, dir, examLinkId);
                String zipUrl = cloudStorageService.uploadFile(zip);
                examLink.setPageImageZipUrl(zipUrl);
                examLinkRepository.updatePageImageZipUrlById(examLinkId, zipUrl);
                logger.info(mm+"zip file created and uploaded: " + (zip.length()/1024)
                        + "K bytes. " + zip.getAbsolutePath()
                        + "\n CloudStorage url: \uD83D\uDD35 " + zipUrl);
                return zip;
            } catch (Exception e) {
                logger.severe(mm+"ERROR: " + e.getMessage());
            }
        } else {
            // Handle the case when the ExamPaper is not found
            throw new IllegalArgumentException("ExamLink not found for id: " + examLinkId);
        }
        throw new Exception("Dunno Boss, something fucked up!!");
    }


    public List<File> convertPdfToImages(File pdfFile, File dir) throws IOException {
        PDDocument document = Loader.loadPDF(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        List<File> imageFiles = new ArrayList<>();
        logger.info(mm+"Document has \uD83D\uDD35\uD83D\uDD35 "
                + document.getNumberOfPages() + " pages ...");
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            try {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 400);
                File imageFile = new File(dir.getPath()+"/image" + page + ".png");
                ImageIO.write(image, "png", imageFile);
                imageFiles.add(imageFile);
            } catch (Exception e) {
                logger.severe(mm+"\uD83D\uDC7F\uD83D\uDC7F\uD83D\uDC7F " +
                        "Unable to render pdf page as image: " + page);
            }
        }

        document.close();
        logger.info(mm+"PDF images created: " + imageFiles.size());
        return imageFiles;
    }

    public File createZipFile(List<File> files, File dir, Long examLinkId) throws IOException {
        File zipFile = new File(dir.getPath()
                + "/pdf_page_image_"+examLinkId+".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new FileOutputStream(zipFile))) {
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(FileUtils.readFileToByteArray(file));
                zipOutputStream.closeEntry();
            }
        }
        return zipFile;
    }

    private void disableCerts() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that accepts all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{new TrustAllCertificates()};

        // Create an SSL context with the trust manager
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        // Disable SSL certificate validation
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        // Enable server hostname verification
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> {
            // Perform proper hostname verification here
            // Return true if the hostname is verified, false otherwise
            // You can use the default hostname verifier for standard verification:
            // return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
            //return true; // Disable hostname verification (not recommended for production)
            return true;
        });
        logger.info(mm+"SSL certificates disabled");
    }
}
