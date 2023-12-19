package com.boha.skunk.services;

import com.boha.skunk.data.*;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.tls.OkHostnameVerifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@SuppressWarnings("StatementWithEmptyBody")
@Service
public class LinkExtractorService {
    private final OkHttpClient client;
    static final String xx = "\uD83E\uDD43\uD83E\uDD43\uD83E\uDD43 LinkExtractorService \uD83E\uDD43";

    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 LinkExtractorService \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(LinkExtractorService.class.getSimpleName());
    @Value("${educUrl}")
    private String educUrl;

    private final ExamLinkService examLinkService;
    private final ExamDocumentRepository examDocumentRepository;
    private final SubjectRepository subjectRepository;

    public LinkExtractorService(ExamLinkService examLinkService, ExamDocumentRepository examDocumentRepository, SubjectRepository subjectRepository) {
        this.examLinkService = examLinkService;
        this.examDocumentRepository = examDocumentRepository;
        this.subjectRepository = subjectRepository;
        this.client = createHttpClient();
    }

    private OkHttpClient createHttpClient() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            HostnameVerifier hostnameVerifier = OkHostnameVerifier.INSTANCE;

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .connectTimeout(60, TimeUnit.SECONDS) // Set the connect timeout to 10 seconds
                    .readTimeout(60, TimeUnit.SECONDS) // Set the read timeout to 10 seconds
                    .hostnameVerifier(hostnameVerifier)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OkHttpClient with custom SSL configuration", e);
        }
    }

    /**
     * Extract exam links from Dept of Education website
     *
     * @return List<ExamLink>
     */
    public List<ExamLink> extractExamLinks() throws IOException {
        List<ExamLink> links = new ArrayList<>();
        List<ExamDocument> docs = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        try {
            Request request = new Request.Builder()
                    .url(educUrl)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String html = response.body().string();
                    // Process the HTML response
                    Document document = Jsoup.parse(html);
                    Element table = document.selectFirst("#dnn_ctr1741_Links_lstLinks");

                    if (table != null) {
                        Elements linkElements = table.select("a[href]");

                        try {
                            for (Element linkElement : linkElements) {
                                String link = linkElement.attr("href");
                                String title = linkElement.text();

                                ExamDocument examDocument = new ExamDocument();
                                examDocument.setTitle(title);
                                examDocument.setLink(convertToAbsoluteLink(link));
                                var resultExamDocument = examDocumentRepository.save(examDocument);
                                docs.add(resultExamDocument);
                                logger.info(mm + " examDocument added to database, id: "
                                        + resultExamDocument.getId() + " title" + resultExamDocument.getTitle());
                                try {
                                    Request mRequest = new Request.Builder()
                                            .url(convertToAbsoluteLink(link))
                                            .build();
                                    try (Response mResponse = client.newCall(mRequest).execute()) {
                                        if (mResponse.isSuccessful() && mResponse.body() != null) {
                                            String html2 = mResponse.body().string();
                                            links = extractDataFromHtml(resultExamDocument, html2);
                                            logger.info(xx + "\n\nLinks and associated " +
                                                    " ... found on the destination page:"
                                                    + resultExamDocument.getTitle() + "  \uD83E\uDD66 links extracted: " + links.size());
                                            logger.info("\n\n\n");
                                        } else {
                                            logger.severe(xx + "Error: \uD83D\uDD34\uD83D\uDD34\uD83D\uDD34 "
                                                    + mResponse.code() + " - " + mResponse.message());
                                        }
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    logger.info("\n\n" + mm + "extractExamLinks: ... total ExamLinks: "
                            + links.size());
                    logger.info(mm + "extractExamLinks: ... total ExamDocuments (top level links): "
                            + docs.size() + "\n\n\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        // Your method logic here

        long endTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (endTime - startTime) / 1000.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedElapsedTime = decimalFormat.format(elapsedTimeInSeconds);

        logger.info(mm + "Elapsed Time: " + formattedElapsedTime + " seconds (minutes) ; " + decimalFormat.format(elapsedTimeInSeconds / 60));
        return links;
    }

    public String convertToAbsoluteLink(String relativeLink) {
        HttpUrl baseHttpUrl = HttpUrl.parse(educUrl);
        assert baseHttpUrl != null;
        HttpUrl absoluteHttpUrl = baseHttpUrl.resolve(relativeLink);
        if (absoluteHttpUrl != null) {
            return absoluteHttpUrl.toString();
        }
        return null;
    }

    public List<ExamLink> extractDataFromHtml(ExamDocument examDocument, String html) {
        logger.info(mm + " extracting data from html .....");
        Document doc = Jsoup.parse(html);
        List<ExamLink> examLinks = new ArrayList<>();
        Elements sections = doc.select(".DnnModule-DNN_Documents");
        logger.info(mm + "Number of DNN_Documents sections: " + sections.size());

        for (Element section : sections) {
            String title = extractTitle(section);
            if (!isEthnicLanguage(title)) {
                var subject = getOrCreateSubject(title);
                //todo - filter ethnic languages
                logger.info(mm + "\uD83D\uDD34 \uD83D\uDD34 " +
                        "Subject Title Extracted: " + subject.getTitle());
                examLinks = extractLinks(section, subject, examDocument);
            }
        }

        logger.info("\n\n" + mm + " ExamDocument extraction complete: " + examDocument.getTitle() +
                " \uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " + examLinks.size());
        return examLinks;
    }

    boolean isEthnicLanguage(String title) {
        if (title.toUpperCase().contains("ISINDEBELE")) {
            return true;
        }
        if (title.toUpperCase().contains("AFRIKAANS")) {
            return true;
        }
        if (title.toUpperCase().contains("ISIXHOSA")) {
            return true;
        }
        if (title.toUpperCase().contains("SISWATI")) {
            return true;
        }
        if (title.toUpperCase().contains("ISIZULU")) {
            return true;
        }
        if (title.toUpperCase().contains("SOUTH AFRICAN SIGN LANGUAGE")) {
            return true;
        }
        if (title.toUpperCase().contains("SEPEDI")) {
            return true;
        }
        if (title.toUpperCase().contains("SETSWANA")) {
            return true;
        }
        if (title.toUpperCase().contains("SESOTHO")) {
            return true;
        }
        if (title.toUpperCase().contains("XITSONGA")) {
            return true;
        }
        if (title.toUpperCase().contains("TSHIVENDA")) {
            return true;
        }

        return false;
    }

    private Subject getOrCreateSubject(String title) {
        String upperCase = title.toUpperCase();
        if (upperCase.contains("MATHEMATICS")) {
            upperCase = "MATHEMATICS";
        }
        if (upperCase.contains("PHYSICAL SCIENCES")) {
            upperCase = "PHYSICAL SCIENCES";
        }
        if (upperCase.contains("RELGIOUS") || upperCase.contains("RELIGIOUS")) {
            upperCase = "RELIGIOUS STUDIES";
        }
        if (upperCase.contains("MATHEMATICS")) {
            upperCase = "MATHEMATICS";
        }
        Subject subject = subjectRepository.findByTitleIgnoreCase(upperCase);
        if (subject == null) {
            subject = new Subject(upperCase);
            subject = subjectRepository.save(subject);
        }
        return subject;
    }

    private String extractTitle(Element section) {
        Element titleElement = section.selectFirst(".eds_containerTitle");
        assert titleElement != null;
        return titleElement.text();
    }

    private List<ExamLink> extractLinks(Element section, Subject subject, ExamDocument examDocument) {
        List<ExamLink> examLinks = new ArrayList<>();
        Elements links = section.select("div.DNN_Documents a");
        for (Element link : links) {
            String linkText = link.text();
            String linkUrl = link.attr("href");
            if (!linkText.equalsIgnoreCase("Download")) {
                String uc = subject.getTitle().toUpperCase();
                if (uc.contains("AFRIKAANS")
                        || uc.contains("SESOTHO")
                        || uc.contains("SETSWANA")
                        || uc.contains("ISIXHOSA")
                        || uc.contains("ISINDEBELE")
                        || uc.contains("ISIZULU")
                        || uc.contains("XITSONGA")
                        || uc.contains("SISWATI")
                        || uc.contains("TSHIVENDA")
                        || uc.contains("SEPEDI")) {
                    // Ignore
                } else {
                    ExamLink examLink0 = new ExamLink();
                    examLink0.setExamDocument(examDocument);
                    examLink0.setLink(convertToAbsoluteLink(linkUrl));
                    examLink0.setTitle(subject.getTitle() + " - " + linkText);
                    examLink0.setDocumentTitle(examDocument.getTitle());
                    examLink0.setSubject(subject);
                    var res = examLinkService.saveExamLink(examLink0);
                    examLinks.add(res);

                }

            }
        }
        logger.info(mm + examLinks.size() + " .... examLinks created for subject: " + subject.getTitle());
        return examLinks;
    }


}