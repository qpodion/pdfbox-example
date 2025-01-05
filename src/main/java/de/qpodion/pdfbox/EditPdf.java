package de.qpodion.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class EditPdf {
    public static void main(String[] args) throws Exception {
        URL templateUrl = ClassLoader.getSystemResource("template.pdf");
        try (PDDocument document = Loader.loadPDF(new File(templateUrl.toURI()))) {
            PDPage page = document.getPage(0);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                PDType1Font font = new PDType1Font(Standard14Fonts.FontName.COURIER);
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(300f, 400f);
                contentStream.showText("Hello World");
                contentStream.endText();
                contentStream.moveTo(0f, 0f);
                contentStream.lineTo(200f, 400f);
                contentStream.stroke();
            }

            try (OutputStream outputStream = Files.newOutputStream(Path.of(System.getProperty("user.dir"), "test.pdf"))) {
                document.save(outputStream);
            }
        }
    }
}
