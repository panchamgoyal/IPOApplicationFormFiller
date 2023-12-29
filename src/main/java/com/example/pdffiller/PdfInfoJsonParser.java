package com.example.pdffiller;

import com.example.pdffiller.entity.PdfInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public class PdfInfoJsonParser {

    public static List<PdfInfo> parseJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<PdfInfo>> typeReference = new TypeReference<List<PdfInfo>>() {};

        return objectMapper.readValue(jsonString, typeReference);
    }
}
