package com.example.pdffiller.service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;



@Service
public class PdfGenerationService {

    /**
     * Fills a PDF template with the provided data and returns the filled PDF as a byte array.
     *
     * @param templatePath   Path to the PDF template.
     * @param outputFileName Output file name.
     * @param name           Name to be filled in the address field.
     * @param email          Email to be filled in the email field.
     * @return Filled PDF as a byte array.
     * @throws IOException If an I/O error occurs.
     */
    public byte[] fillAndFlattenPdfTemplate(String templatePath, String outputFileName, String name, String email) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfReader reader = new PdfReader(templatePath);
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(reader, writer)) {
            PdfPage page = pdf.getPage(1);
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);


            Rectangle fieldAddressRectangle = new Rectangle(340, 705, 243, 9);
            PdfFormField textFieldAddress = PdfFormField.createText(pdf, fieldAddressRectangle, "address");
            // Add the text field to the form
            textFieldAddress.setFontSize(7);
            textFieldAddress.setValue(name);
            textFieldAddress.setReadOnly(true);
            form.addField(textFieldAddress, page);
            Rectangle fieldEmailRectangle = new Rectangle(448, 694, 135, 9);
            PdfFormField textFieldEmail = PdfFormField.createText(pdf, fieldEmailRectangle, "email");
            // Add the text field to the form
            textFieldEmail.setFontSize(7);
            textFieldEmail.setValue(email);
            textFieldEmail.setReadOnly(true);
            form.addField(textFieldEmail, page);

            // Flatten form fields
            form.flattenFields();

            // Close the documents
            pdf.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
            throw e; // Propagate the exception if needed
        }
    }



}
