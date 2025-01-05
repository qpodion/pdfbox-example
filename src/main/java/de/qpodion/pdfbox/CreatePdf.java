package de.qpodion.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreatePdf {
    public static void main(String[] args) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType1Font font = new PDType1Font(Standard14Fonts.FontName.COURIER);
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(300f, 400f);
                contentStream.showText("Hello World");
                contentStream.endText();
                contentStream.moveTo(0f, 0f);
                contentStream.lineTo(200f, 400f);
                contentStream.stroke();

                try (ByteArrayOutputStream qrCodeStream = new ByteArrayOutputStream()) {
                    QRCode.create(qrCodeStream, "ABC123", 200, 200);
                    PDImageXObject qrCode = PDImageXObject.createFromByteArray(document, qrCodeStream.toByteArray(), "qr-code-abc123");
                    contentStream.drawImage(qrCode, 0f, 0f);
                }
            }

            try (OutputStream outputStream = Files.newOutputStream(Path.of(System.getProperty("user.dir"), "test.pdf"))) {
                document.save(outputStream);
            }
        }
    }
}
