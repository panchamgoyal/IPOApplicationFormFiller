package com.example.pdffiller.service;

import com.example.pdffiller.entity.PdfInfo;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;



@Service
public class PdfGenerationService {

    public byte[] fillAndFlattenPdfTemplate(byte[] pdfBytes, PdfInfo pdfInfo) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
             PdfReader reader = new PdfReader(inputStream);
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(reader, writer)) {


            PdfPage page = pdf.getPage(1);
            PdfCanvas canvas = new PdfCanvas(page);

            // Set font properties
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
            float fontSize = 7;
            Color textColor = new DeviceRgb(0, 0, 0);

            // Add text to the PDF
            canvas.beginText().setFontAndSize(font, fontSize).setColor(textColor, true)
                    .moveText(340, 706).showText(pdfInfo.getAddress())
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
            canvas.beginText().setFontAndSize(font, 14).setColor(textColor, true)
                    .setCharacterSpacing(20)
                    .moveText(310, 648).showText(pdfInfo.getPanOfSole().toUpperCase())
                    .endText();
            canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                    .setCharacterSpacing(10)
                    .moveText(423, 683).showText(pdfInfo.getTelNumber())
                    .endText();
            canvas.beginText().setFontAndSize(font, 14).setColor(textColor, true)
                    .setCharacterSpacing(20)
                    .moveText(20, 610).showText(pdfInfo.getBidderDepositoryAccountDetails())
                    .endText();
            if(pdfInfo.getNsdl()){
            canvas.beginText().setFontAndSize(font, 12).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(215.5, 629.1).showText("X")
                    .endText();
            }
            else{
                canvas.beginText().setFontAndSize(font, 12).setColor(textColor, true)
                        .setCharacterSpacing(0)
                        .moveText(258, 629.1).showText("X")
                        .endText();
            }

            addPaymentDetails(canvas, font, textColor, pdfInfo);


            canvas.beginText().setFontAndSize(font, 10).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(130, 88).showText(pdfInfo.getAmmountBlocked())
                    .endText();
//            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
//                    .setCharacterSpacing(0)
//                    .moveText(121, 73).showText(pdfInfo.getUpiId())
//                    .endText();
            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(121, 73).showText(pdfInfo.getBankAccountNumber())
                    .endText();
            canvas.beginText().setFontAndSize(font, 7).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(106, 60).showText(pdfInfo.getBankNameAndBranch())
                    .endText();
            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(86, 158).showText(pdfInfo.getTelNumber())
                    .endText();
            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(244, 159).showText(pdfInfo.getEmail())
                    .endText();
            canvas.beginText().setFontAndSize(font, 7).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(85, 175).showText(pdfInfo.getName())
                    .endText();
            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(84, 189).showText(pdfInfo.getBankNameAndBranch())
                    .endText();
            canvas.beginText().setFontAndSize(font, 10).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(102, 203).showText(pdfInfo.getAmmountBlocked())
                    .endText();
//            canvas.beginText().setFontAndSize(font, 7).setColor(textColor, true)
//                    .setCharacterSpacing(0)
//                    .moveText(249, 204).showText(pdfInfo.getUpiId())
//                    .endText();
            canvas.beginText().setFontAndSize(font, 7).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(249, 204).showText(pdfInfo.getBankAccountNumber())
                    .endText();
            canvas.beginText().setFontAndSize(font, 14).setColor(textColor, true)
                    .setCharacterSpacing(13)
                    .moveText(37, 229).showText(pdfInfo.getBidderDepositoryAccountDetails())
                    .endText();
            canvas.beginText().setFontAndSize(font, 14).setColor(textColor, true)
                    .setCharacterSpacing(12)
                    .moveText(392, 229).showText(pdfInfo.getPanOfSole())
                    .endText();

            addCheckBoxes(canvas, font, textColor, pdfInfo);
            addBidOption(canvas, font, textColor, pdfInfo);
            addBidOptionFooter(canvas, font, textColor, pdfInfo);

            canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(405, 120).showText(pdfInfo.getName())
                    .endText();

            // Close the documents
            pdf.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void addBidOptionFooter(PdfCanvas canvas, PdfFont font, Color textColor, PdfInfo pdfInfo) {
        canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                .setCharacterSpacing(0)
                .moveText(102, 115).showText(pdfInfo.getNumberOfEquityShare())
                .endText();
        canvas.beginText().setFontAndSize(font, 8).setColor(textColor, true)
                .setCharacterSpacing(0)
                .moveText(102, 102).showText(pdfInfo.getBidPrice())
                .endText();
    }

    private void addBidOption(PdfCanvas canvas, PdfFont font, Color textColor, PdfInfo pdfInfo){
        canvas.beginText().setFontAndSize(font, 9).setColor(textColor, true)
                .setCharacterSpacing(0)
                .moveText(323, 524).showText(pdfInfo.getAmmountBlocked())
                .endText();
        canvas.beginText().setFontAndSize(font, 9).setColor(textColor, true)
                .setCharacterSpacing(0)
                .moveText(208, 524).showText(pdfInfo.getBidPrice())
                .endText();
        canvas.beginText().setFontAndSize(font, 10).setColor(textColor, true)
                .setCharacterSpacing(13)
                .moveText(56, 524).showText(pdfInfo.getNumberOfEquityShare())
                .endText();
    }
    private void addCheckBoxes(PdfCanvas canvas, PdfFont font, Color textColor, PdfInfo pdfInfo){
        if(Long.parseLong(pdfInfo.getAmmountBlocked()) < 200000) {
            canvas.beginText().setFontAndSize(font, 12).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(390, 525).showText("X")
                    .endText();
            canvas.beginText().setFontAndSize(font, 12).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(412.8, 560).showText("X")
                    .endText();
        }
        else {
            canvas.beginText().setFontAndSize(font, 12).setColor(textColor, true)
                    .setCharacterSpacing(0)
                    .moveText(412.8, 528).showText("X")
                    .endText();
        }
    }
    private void addPaymentDetails(PdfCanvas canvas, PdfFont font, Color textColor, PdfInfo pdfInfo){
        canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                .setCharacterSpacing(6)
                .moveText(117, 468).showText(pdfInfo.getAmmountBlocked())
                .endText();
        canvas.beginText().setFontAndSize(font, 9).setColor(textColor, true)
                .setCharacterSpacing(0)
                .moveText(295, 469).showText(pdfInfo.getAmmountInWords())
                .endText();
        if(pdfInfo.getBankAccountNumber() != null && !pdfInfo.getBankAccountNumber().equals("")) {
            if (pdfInfo.getBankAccountNumber().length() <= 10 && pdfInfo.getBankAccountNumber().length() != 0) {
                canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
                        .setCharacterSpacing(5)
                        .moveText(68, 450).showText(pdfInfo.getBankAccountNumber())
                        .endText();
            } else {
                if (pdfInfo.getBankAccountNumber().length() > 10) {
                    String bankAccountNumber = pdfInfo.getBankAccountNumber();
                    int startIndex = 0;
                    int endIndex = 10;
                    int startX = 18;
                    while (startIndex < bankAccountNumber.length()) {
                        String substring = bankAccountNumber.substring(startIndex, endIndex);

                        // Render each substring
                        canvas.beginText()
                                .setFontAndSize(font, 11)
                                .setColor(textColor, true)
                                .setCharacterSpacing(5)
                                .moveText(startX + 49, 450)
                                .showText(substring)
                                .endText();

                        // Move to the next substring
                        startX += 114;
                        startIndex += 10;
                        endIndex = Math.min(endIndex + 10, bankAccountNumber.length());
                    }
                }
            }
        }

        canvas.beginText().setFontAndSize(font, 10).setColor(textColor, true)
                .moveText(81, 434).showText(pdfInfo.getBankNameAndBranch())
                .endText();
//        if(pdfInfo.getUpiId() != null && !pdfInfo.getUpiId().equals("")) {
//            if (pdfInfo.getUpiId().length() <= 10 && pdfInfo.getUpiId().length() != 0) {
//                canvas.beginText().setFontAndSize(font, 11).setColor(textColor, true)
//                        .setCharacterSpacing(5)
//                        .moveText(72, 415).showText(pdfInfo.getUpiId())
//                        .endText();
//            } else {
//                if (pdfInfo.getUpiId().length() > 10) {
//                    String upiId = pdfInfo.getUpiId();
//                    int startIndex = 0;
//                    int endIndex = 10;
//                    int startX = 20;
//                    while (startIndex < upiId.length()) {
//                        String substring = upiId.substring(startIndex, endIndex);
//
//                        // Render each substring
//                        canvas.beginText()
//                                .setFontAndSize(font, 11)
//                                .setColor(textColor, true)
//                                .setCharacterSpacing(5)
//                                .moveText(startX + 49, 415)
//                                .showText(substring)
//                                .endText();
//
//                        // Move to the next substring
//                        startX += 113;
//                        startIndex += 10;
//                        endIndex = Math.min(endIndex + 10, upiId.length());
//                    }
//                }
//            }
//        }
    }

    private static String getFirst15Characters(String input) {
        return input.substring(0, Math.min(input.length(), 15));
    }

    private static String getRestOfCharacters(String input) {
        return input.substring(Math.min(input.length(), 15));
    }


}
