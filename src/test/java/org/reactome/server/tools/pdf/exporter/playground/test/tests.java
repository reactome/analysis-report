package org.reactome.server.tools.pdf.exporter.playground.test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Date;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@SpringBootTest
public class tests {
    @Test
    public void test() throws  Exception{
        File file = new File("src/main/resources/pdfs/"+new Date().getTime()+".pdf");
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));
        Document document = new Document(pdfDocument);
        Table table = new Table(3);
        Cell cell = new Cell(1,3).add("231431243214312");
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addHeaderCell(cell);
//        table.addHeaderCell("123");
//        table.addHeaderCell("123");
//        table.addHeaderCell("123");
        table.addCell("qwe");
        table.addCell("qwe");
        table.addCell("qwe");
        table.addCell("qwe");
        document.add(table);
        document.close();
        pdfDocument.close();
    }
}
