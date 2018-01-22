package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelperTest {

    @Test
    public void test() throws Exception {
        String stId = "R-HSA-169911";
        BufferedImage diagram = new DiagramHelper().getDiagram(stId, new ReportArgs("MjAxNzEyMTgwNjM0MDJfMjI%253D", "/home/byron/static/demo", "/home/byron/static", "/home/byron/json"));
        File file = new File("src/test/resources/diagrams/" + stId + ".jpg");
        ImageIO.write(diagram, "jpg", file);
    }
}