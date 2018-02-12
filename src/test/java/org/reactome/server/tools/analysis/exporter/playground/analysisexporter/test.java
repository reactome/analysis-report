package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class test {
    @Test
    public void largeTableTest() throws FileNotFoundException {
        PdfDocument document = new PdfDocument(new PdfWriter("src/test/resources/pdfs/demo.pdf"));
        Document doc = new Document(document);
        Table table = new Table(5, true);

        for (int i = 0; i < 5; i++) {
            table.addHeaderCell(new Cell().setKeepTogether(true).add(new Paragraph("Header " + i)));
        }

        doc.add(table);
        for (int i = 0; i < 1000; i++) {
            if (i % 5 == 0) {
                table.flush();
            }
            table.addCell(new Cell().setKeepTogether(true).add(new Paragraph("Test " + i).setMargins(0, 0, 0, 0)));
        }

        table.complete();

        doc.close();
    }
}
