package org.reactome.server.tools.pdf.exporter.playground.diagramexporter;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@SpringBootTest
public class DiagramExportDemoTests {

    @Test
    public void test() throws Exception{
        assertEquals(BufferedImage.class,DiagramExporter.getBufferedImage("R-HSA-391160"));
    }
}
