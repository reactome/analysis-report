package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
public class DiagramHelperTest {

    @Test
    public void test() throws Exception {
        String stId = "R-HSA-169911";
        BufferedImage diagram = DiagramHelper.getDiagram(stId, new ReportArgs("", "/home/byron/static/demo", "/home/byron/static", "/home/byron/json"));
        final File file = new File("src/test/resources/diagrams/" + stId + ".png");
        ImageIO.write(diagram, "png", file);
    }
}