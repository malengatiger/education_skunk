package com.boha.skunk.controllers;

import com.boha.skunk.data.ExamLink;
import com.boha.skunk.services.PdfService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.logging.Logger;

@Controller
@RequestMapping("/pdf")
public class PdfController {
    static final String mm = "\uD83C\uDF3C\uD83C\uDF3C\uD83C\uDF3C\uD83C\uDF3C" +
            " PdfController \uD83C\uDF3C";
    static final Logger logger = Logger.getLogger(PdfController.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    private final PdfService pdfService;

    @Autowired
    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/createPdfPageImages")
    public ResponseEntity<ExamLink> createPdfPageImages(@RequestParam Long examLinkId) throws Exception {

        ExamLink examLink = pdfService.getPdfPageImages(examLinkId);
//        byte[] zipBytes = FileUtils.readFileToByteArray(zipFile);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", "images_"+examLinkId+".zip");
        logger.info(mm+"Return updated ExamLink: " + examLink.getPageImageZipUrl()
        + ", id: " + (examLink.getId()));
        return ResponseEntity.ok().body(examLink);
    }
}
