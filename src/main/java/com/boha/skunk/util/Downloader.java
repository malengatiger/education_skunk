package com.boha.skunk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Downloader {
    static final String mm = "\uD83E\uDD6C \uD83E\uDD6C \uD83E\uDD6C " +
            "Downloader  \uD83E\uDD6C";
    static final Logger logger = Logger.getLogger(Downloader.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public static File downloadPdf(String url) throws IOException {

        //
        //
        // Disable SSL certificate validation
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        URL pdfUrl = new URL(url);
        File dir = createDirectoryIfNotExists();
        BufferedInputStream in = new BufferedInputStream(pdfUrl.openStream());
        File outputFile = new File(dir.getPath()+"/pdf_"
                + System.currentTimeMillis() + ".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        in.close();
        logger.info(mm + "File downloaded: \uD83D\uDD35 " + outputFile.length() + " bytes "
                + " \uD83D\uDD35 path: " + outputFile.getAbsolutePath());
        return outputFile;
    }
    public static File createDirectoryIfNotExists() throws IOException {
        Path path = Paths.get("pdfs/exam//@Tables");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }
}
