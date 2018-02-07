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

    private static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    private static final String diagramPath = "/home/byron/static/demo";
    private static final String ehldPath = "/home/byron/static";
    private static final String fireworksPath = "/home/byron/json";

    @Test
    public void test() throws Exception {
        String stId = "R-HSA-15869";
        BufferedImage diagram = DiagramHelper.getDiagram(stId, new ReportArgs(token, diagramPath, ehldPath, fireworksPath));

        File file = new File("src/test/resources/diagrams/" + stId + ".png");
        ImageIO.write(diagram, "png", file);
    }
}