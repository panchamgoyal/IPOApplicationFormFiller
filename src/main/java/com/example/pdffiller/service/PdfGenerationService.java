package com.example.pdffiller.service;

import com.example.pdffiller.entity.PdfInfo;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;



@Service
public class PdfGenerationService {

    public byte[] fillAndFlattenPdfTemplate(String templatePath, String outputFileName, PdfInfo pdfInfo) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfReader reader = new PdfReader(templatePath);
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(reader, writer)) {

            // ... your existing code to fill form fields

            // Overlay additional text
            PdfPage page = pdf.getPage(1);
            PdfCanvas canvas = new PdfCanvas(page);

            // Set font properties
            PdfFont font = PdfFontFactory.createFont(FontConstants.COURIER);
            float fontSize = 7;
            Color textColor = new DeviceRgb(0, 0, 0);

            // Add text to the PDF
            canvas.beginText().setFontAndSize(font, fontSize).setColor(textColor, true)
                    .moveText(340, 705).showText(pdfInfo.getAddress())
                    .endText();
            canvas.beginText().setFontAndSize(font, fontSize).setColor(textColor, true)
                    .moveText(448, 694).showText(pdfInfo.getEmail())
                    .endText();
            if(pdfInfo.getName().length() <= 15) {
                String name = pdfInfo.getName();
                name = name.toUpperCase();
                canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                        .setCharacterSpacing(10)
                        .moveText(345, 731).showText(name)
                        .endText();
            }
            else {
                String name = pdfInfo.getName();
                String name1 = getFirst15Characters(name);
                String name2 = getRestOfCharacters(name);
                name1 = name1.toUpperCase();
                name2 = name2.toUpperCase();
                canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                        .setCharacterSpacing(10)
                        .moveText(345, 731).showText(name1)
                        .endText();
                canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                        .setCharacterSpacing(10)
                        .moveText(309, 717).showText(name2)
                        .endText();
            }

            String panOfSole = pdfInfo.getPanOfSole().toUpperCase();
            canvas.beginText().setFontAndSize(font, 14).setColor(textColor, true)
                    .setCharacterSpacing(20)
                    .moveText(310, 648).showText(panOfSole)
                    .endText();

            canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                    .setCharacterSpacing(10)
                    .moveText(423, 683).showText(pdfInfo.getTelNumber())
                    .endText();
            // Close the documents
            pdf.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
            throw e; // Propagate the exception if needed
        }
    }

    private static String getFirst15Characters(String input) {
        return input.substring(0, Math.min(input.length(), 15));
    }

    private static String getRestOfCharacters(String input) {
        return input.substring(Math.min(input.length(), 15));
    }


}
