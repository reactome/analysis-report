package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class itext {
    public void create() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("src/test/resources/pdfs/" + Instant.now().toEpochMilli() + ".pdf"));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfDocument document = new PdfDocument(new PdfWriter(fileOutputStream));
        Document pdf = new Document(document, PageSize.A4);
        Paragraph paragraph = new Paragraph();
        for (int i = 0; i < 50; i++) {
            pdf.add(new Paragraph().add("gfaregregveargbvearrrrg73489y5th32uytghhhhhhhhho85gt4frblgfverw8oooooooogfelvbersuigf783gto54f4wwwwwwwwwwwwgfw8p4o5gfblvp9874gt54hg"));
            pdf.add(new Image(ImageDataFactory.create("src/test/resources/diagrams/R-HSA-169911.png")).scale(0.1f, 0.1f));
            document.getWriter().flush();
        }
        pdf.close();
        document.close();
    }

    @Test
    public void test() throws IOException {
        for (File file : new File("src/test/resources/pdfs").listFiles()) file.delete();
        for (int i = 0; i < 5; i++) {
            create();
        }
    }
}
