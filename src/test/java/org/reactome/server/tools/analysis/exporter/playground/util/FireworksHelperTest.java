package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */


public class FireworksHelperTest {

    private static final String diagramPath = "/home/byron/reactome/diagram";
    private static final String ehldPath = "/home/byron/reactome/ehld";
    private static final String svgSummary = "/home/byron/reactome/ehld/svgSummary.txt";
    private static final String fireworksPath = "/home/byron/reactome/fireworks";
    private static final String analysisPath = "src/test/resources/analysis";

    @Test
    public void export() throws Exception {
        ReportArgs reportArgs = new ReportArgs("MjAxODAyMTIxMTI5MzdfMQ==", diagramPath, ehldPath, fireworksPath, analysisPath, svgSummary);
        AnalysisStoredResult analysisStoredResult = new TokenUtils("src/test/resources/analysis").getFromToken(reportArgs.getToken());
        FireworksHelper.setPaths(reportArgs);
        BufferedImage fireworks = FireworksHelper.getFireworks(analysisStoredResult);
        if (fireworks != null) {
            ImageIO.write(fireworks, "png", new File("src/test/resources/fireworks/MjAxODAyMTIxMTI5MzdfMQ.png"));
        }
    }
}