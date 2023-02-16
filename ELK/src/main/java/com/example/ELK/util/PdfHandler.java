package com.example.ELK.util;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PdfHandler {

    public String parseFile(File file) {
        return getText(file);
    }

    public String getText(File file) {
        try {
            PDFParser parser = getPDFParser(file);
            return getText(parser);
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }

    public PDFParser getPDFParser(File file) throws IOException {
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        return parser;
    }

    public String getText(PDFParser parser) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        return textStripper.getText(parser.getPDDocument());
    }
}
