package com.example.pdffiller.controller;

import com.example.pdffiller.entity.PdfInfo;
import com.example.pdffiller.repository.PdfInfoRepository;
import com.example.pdffiller.service.PdfGenerationService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;
    @Autowired
    private PdfInfoRepository pdfInfoRepository;

    @PostMapping(value = "/fillPdfTemplateAndCompress", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> fillPdfTemplateAndCompress(@RequestPart("templateZip") MultipartFile templateZip) throws IOException {
        // Create a temporary output stream for the filled ZIP file
        try(ByteArrayOutputStream filledZipOutputStream = new ByteArrayOutputStream()) {

            try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(filledZipOutputStream)) {

                try (ZipInputStream zipInputStream = new ZipInputStream(templateZip.getInputStream())) {
                    ZipEntry entry;
                    int i = 1;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        String pdfFilename = entry.getName();
                        // Check if the entry is a PDF file
                        if (!entry.isDirectory() && !pdfFilename.startsWith("__MACOSX/") && pdfFilename.toLowerCase().endsWith(".pdf")) {
                            // Get the corresponding PdfInfo from the database
                            PdfInfo pdfInfo = pdfInfoRepository.findById((long) i).orElse(null);

                            if (pdfInfo != null) {
                                // Process the PDF
                                ZipArchiveEntry zipEntry = new ZipArchiveEntry(pdfFilename);
                                zipOutputStream.putArchiveEntry(zipEntry);

                                byte[] pdfBytes = IOUtils.toByteArray(zipInputStream);
                                byte[] filledPdfBytes = pdfGenerationService.fillAndFlattenPdfTemplate(pdfBytes, pdfInfo);

                                zipOutputStream.write(filledPdfBytes);
                                zipOutputStream.closeArchiveEntry();

                                i++;
                            }
                            else{
                                return ResponseEntity.badRequest().build();
                            }
                        }
                    }
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "output_filled.zip");
            byte[] zipFilled = filledZipOutputStream.toByteArray();
            filledZipOutputStream.close();
            return ResponseEntity.ok().headers(headers).body(zipFilled);
        }
    }



}
