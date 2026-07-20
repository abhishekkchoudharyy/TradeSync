package com.abhishekkchoudharyy.tradesync.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;


@Service
public class PdfTextExtractionService {

    public String extractTextFromFile(String filePath) {
        File file = new File(filePath);

        //Always check if the file actually exists
        if (!file.exists()) {
            throw new RuntimeException("Cannot extract text: File not found at " + filePath);
        }

        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException("Error extracting text from PDF: " + filePath, e);
        }
    }
}