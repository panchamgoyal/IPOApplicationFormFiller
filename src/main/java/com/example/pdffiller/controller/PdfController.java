package com.example.pdffiller.controller;

import com.example.pdffiller.entity.PdfInfo;
import com.example.pdffiller.repository.PdfInfoRepository;
import com.example.pdffiller.service.PdfGenerationService;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;
    @Autowired
    private PdfInfoRepository pdfInfoRepository;

    @GetMapping("/fill-template-and-compress")
    public ResponseEntity<byte[]> fillPdfTemplateAndCompress() throws IOException {
        String templatePath = "D:\\PdfEditor\\PdfFiller\\src\\main\\resources\\templates\\TATATECH_800121598.pdf"; // Replace with the actual path
        String outputFileName = "output_filled.zip";

        List<PdfInfo> pdfInfos = pdfInfoRepository.findAllByFilledIsFalse();
        List<byte[]> pdfBytesForMore = pdfInfos.stream().map(pdfInfo -> {
            try {
                return pdfGenerationService.fillAndFlattenPdfTemplate(templatePath, outputFileName, pdfInfo.getAddress(), pdfInfo.getEmail());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        byte[] zipBytes = createZipArchive(pdfBytesForMore);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", outputFileName);

        return ResponseEntity.ok().headers(headers).body(zipBytes);
    }

    private byte[] createZipArchive(List<byte[]> pdfBytesList) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream)) {

            for (int i = 0; i < pdfBytesList.size(); i++) {
                byte[] pdfBytes = pdfBytesList.get(i);
                ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfBytes);

                ZipArchiveEntry entry = new ZipArchiveEntry("pdf" + i + ".pdf");
                zipOutputStream.putArchiveEntry(entry);
                IOUtils.copy(pdfInputStream, zipOutputStream);
                zipOutputStream.closeArchiveEntry();
            }

            zipOutputStream.finish();

            return outputStream.toByteArray();
        }
    }
}
