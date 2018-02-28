package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelperTest {
    private static final String TOKEN_OVER01 = "MjAxODAyMTIxMTI5MzdfMQ==";
    private static final String diagramPath = "/home/byron/reactome/diagram";
    private static final String ehldPath = "/home/byron/reactome/ehld";
    private static final String svgSummary = "/home/byron/reactome/ehld/svgSummary.txt";
    private static final String fireworksPath = "/home/byron/reactome/fireworks";
    private static final String analysisPath = "src/test/resources/analysis";
    private static final TokenUtils TOKEN_UTILS = new TokenUtils(analysisPath);
    private static final RasterExporter EXPORTER = new RasterExporter(diagramPath, ehldPath, analysisPath, svgSummary);
    private static DiagramService DIAGRAM_SERVICE;

    @BeforeClass
    public static void setUp() {
        ReactomeGraphCore.initialise(
                System.getProperty("neo4j.host")
                , System.getProperty("neo4j.port")
                , System.getProperty("neo4j.user")
                , System.getProperty("neo4j.password")
                , GraphCoreConfig.class);
        DIAGRAM_SERVICE = ReactomeGraphCore.getService(DiagramService.class);
    }

    @Test
    public void export() throws IOException {
        ReportArgs reportArgs = new ReportArgs("MjAxODAyMTIxMTI5MzdfMQ==", diagramPath, ehldPath, fireworksPath, analysisPath, svgSummary);
        DiagramHelper.setPaths(reportArgs);
        AnalysisStoredResult result = new TokenUtils("src/test/resources/analysis").getFromToken(reportArgs.getToken());
        BufferedImage diagram = DiagramHelper.getDiagram("R-HSA-1226099", result, "total");
        if (diagram != null) {
            ImageIO.write(diagram, "png", new File("src/test/resources/diagrams/R-HSA-1226099.png"));
        }
    }

    @Test
    public void testMeasureTime() {
        final AnalysisStoredResult result = TOKEN_UTILS.getFromToken(TOKEN_OVER01);
        long start = System.nanoTime();
        result.getPathways().stream()
                .limit(25)
                .forEach(pathway -> {
                    DiagramResult diagramResult = DIAGRAM_SERVICE.getDiagramResult(pathway.getStId());
                    try {
                        RasterArgs rasterArgs = new RasterArgs(diagramResult.getDiagramStId(), "png");
                        rasterArgs.setSelected(diagramResult.getEvents());
                        rasterArgs.setQuality(5);
                        EXPORTER.export(rasterArgs, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        long time = System.nanoTime() - start;
        System.out.println(time / 1e6 + " ms");

    }


}