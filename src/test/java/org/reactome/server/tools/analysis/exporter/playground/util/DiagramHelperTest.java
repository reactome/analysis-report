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

    static final String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
    static final String diagramPath = "/home/byron/static/demo";
    static final String ehldPath = "/home/byron/static";
    static final String fireworksPath = "/home/byron/json";

    @Test
    public void test() throws Exception {
        String stId = "R-HSA-163685";
        BufferedImage diagram = DiagramHelper.getDiagram(stId, new ReportArgs(token, diagramPath, ehldPath, fireworksPath));
        File file = new File("src/test/resources/diagrams/" + stId + ".png");
        ImageIO.write(diagram, "png", file);
    }
}