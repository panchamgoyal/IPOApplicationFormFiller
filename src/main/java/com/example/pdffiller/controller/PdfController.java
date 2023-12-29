package com.example.pdffiller.controller;

import com.example.pdffiller.PdfInfoJsonParser;
import com.example.pdffiller.entity.PdfInfo;
import com.example.pdffiller.service.PdfGenerationService;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;
    @PostMapping(value = "/fillPdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> fillPdfTemplateAndCompress(
            @Parameter(description = "List of PDF files", required = true, allowEmptyValue = false)
            @RequestPart("pdfFiles") List<MultipartFile> pdfFiles,

            @Parameter(description = "List of pdfInfo", required = true, allowEmptyValue = false)
            @RequestPart("pdfInfos") String pdfInfoDtoList) throws IOException {

        List<PdfInfo> pdfInfoList = PdfInfoJsonParser.parseJsonString(pdfInfoDtoList);
        try (ByteArrayOutputStream filledZipOutputStream = new ByteArrayOutputStream()) {
            try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(filledZipOutputStream)) {
                int i = 0;

                for (MultipartFile pdfFile : pdfFiles) {
                    String pdfFilename = pdfFile.getOriginalFilename();

                    if (pdfFilename != null && pdfFilename.toLowerCase().endsWith(".pdf")) {
                        // Get PdfInfo for the current PDF
                        PdfInfo pdfInfo = pdfInfoList.get(i);

                        // Process the PDF
                        byte[] pdfBytes = pdfFile.getBytes();
                        byte[] filledPdfBytes = pdfGenerationService.fillAndFlattenPdfTemplate(pdfBytes, pdfInfo);

                        ZipArchiveEntry zipEntry = new ZipArchiveEntry(pdfFilename);
                        zipOutputStream.putArchiveEntry(zipEntry);
                        zipOutputStream.write(filledPdfBytes);
                        zipOutputStream.closeArchiveEntry();

                        i++;
                    }
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "output_filled.zip");
            byte[] zipFilled = filledZipOutputStream.toByteArray();
            return ResponseEntity.ok().headers(headers).body(zipFilled);
        }
    }

    @PostMapping(value = "/fillPdfZip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> fillPdfTemplateAndCompress(
            @Parameter(description = "List of PDF files", required = true, allowEmptyValue = false)
            @RequestPart("templateZip") MultipartFile templateZip,
            @Parameter(description = "List of pdfInfo", required = true, allowEmptyValue = false)
            @RequestPart("pdfInfos") String pdfInfoDtoList) throws IOException {

        List<PdfInfo> pdfInfoList = PdfInfoJsonParser.parseJsonString(pdfInfoDtoList);
        // Create a temporary output stream for the filled ZIP file
        try (ByteArrayOutputStream filledZipOutputStream = new ByteArrayOutputStream()) {
            try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(filledZipOutputStream)) {
                try (ZipInputStream zipInputStream = new ZipInputStream(templateZip.getInputStream())) {
                    ZipEntry entry;
                    int i = 0;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        String pdfFilename = entry.getName();
                        // Check if the entry is a PDF file
                        if (!entry.isDirectory() && !pdfFilename.startsWith("__MACOSX/") && pdfFilename.toLowerCase().endsWith(".pdf")) {
                            // Get the corresponding PdfInfo from the JSON data
                            PdfInfo pdfInfo = pdfInfoList.get(i);

                            // Process the PDF
                            ZipArchiveEntry zipEntry = new ZipArchiveEntry(pdfFilename);
                            zipOutputStream.putArchiveEntry(zipEntry);

                            byte[] pdfBytes = IOUtils.toByteArray(zipInputStream);
                            byte[] filledPdfBytes = pdfGenerationService.fillAndFlattenPdfTemplate(pdfBytes, pdfInfo);

                            zipOutputStream.write(filledPdfBytes);
                            zipOutputStream.closeArchiveEntry();

                            i++;
                        }
                    }
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "output_filled.zip");
            byte[] zipFilled = filledZipOutputStream.toByteArray();
            return ResponseEntity.ok().headers(headers).body(zipFilled);
        }
    }



}


