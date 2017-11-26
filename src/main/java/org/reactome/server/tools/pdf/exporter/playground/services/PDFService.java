package org.reactome.server.tools.pdf.exporter.playground.services;//package com.example.demo.services;
//
//import com.example.demo.controller.demoController;
//import com.example.demo.itext.ItextDemo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
///**
// * @author Chuan Deng <cdeng@ebi.ac.uk>
// */
//@Service
//public class PDFService {
//    @Autowired
//    private ItextDemo itextDemo;
//
//    public ResponseEntity<InputStreamResource> getPDFByToken(String token) throws Exception {
//        String reg = "\\s+";
//        Logger logger = LoggerFactory.getLogger(demoController.class);
//        long start = System.currentTimeMillis();
//        FileSystemResource fileSystemResource = new FileSystemResource("");
////        FileSystemResource fileSystemResource = new FileSystemResource("C:\\Users\\Byron\\PDFs\\" + token + ".pdf");
//        logger.info(token + ":" + token.length());
//
//        if (!fileSystemResource.exists())
//            fileSystemResource = itextDemo.test(token);
//        long end = System.currentTimeMillis();
//        logger.warn("create pdf file complete in:{}", end - start);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        httpHeaders.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileSystemResource.getFilename()));
//        httpHeaders.add("Pragma", "no-cache");
//        httpHeaders.add("Expires", "0");
//        return ResponseEntity
//                .ok()
//                .headers(httpHeaders)
//                .contentLength(fileSystemResource.contentLength())
//                .contentType(MediaType.parseMediaType("application/pdf"))
//                .body(new InputStreamResource(fileSystemResource.getInputStream()));
//    }
//}
