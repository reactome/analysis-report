package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {
    // Max QUALITY = 6 --> Max factor = 1.4f and factor must less than 3.
    private static final float MAX_FACTOR = 1.4f;
    private static final Integer MAX_WIDTH = 3000;
    private static final Integer MIN_WIDTH = 700;
    private static Integer QUALITY = 5;
    private static RasterExporter exporter;
    private static DiagramService DIAGRAM_SERVICE;

    static {
        ReactomeGraphCore.initialise(System.getProperty("neo4j.host"), System.getProperty("neo4j.port"), System.getProperty("neo4j.user"), System.getProperty("neo4j.password"), GraphCoreConfig.class);
        DIAGRAM_SERVICE = ReactomeGraphCore.getService(DiagramService.class);
    }

    /**
     * create pathway's diagram.
     *
     * @param stId   stable identifier of the diagram.
     * @param result {@see AnalysisStoredResult}.
     * @return {@see BufferedImage}.
     */
    public static BufferedImage getPNGDiagram(String stId, AnalysisStoredResult result, String resource) {
        DiagramResult diagramResult = DIAGRAM_SERVICE.getDiagramResult(stId);
        RasterArgs args = new RasterArgs(diagramResult.getDiagramStId(), "png");
//        float factor = 1;
//        float threshold = 4 * width;
//        System.out.println("before : " + diagramResult.getWidth() + "x" + diagramResult.getHeight());
//        if (diagramResult.getWidth() < MIN_WIDTH) {
//            factor = MIN_WIDTH/diagramResult.getWidth() ;
//        } else if (diagramResult.getWidth() > MAX_WIDTH) {
//            factor =  MAX_WIDTH/diagramResult.getWidth();
//        } else {
//            System.out.println("no change");
//        }
//        else if (height > diagramResult.getHeight()) {
//            factor = height / diagramResult.getHeight();
//        }
//        if (factor != 1) {
//            args.setQuality(getQuality(factor));
//        }
        /*
         * since quality is Integer so factors cant influence quality directly(the decimal will been ignored when been cast to int),
         * so for image which is fuzzy just increase the quality to 6 and reduce the quality to 4 for those width is large.
         */
//        if (diagramResult.getWidth() < MIN_WIDTH) {
//            args.setQuality(QUALITY + 1);
//        } else if (diagramResult.getWidth() > MAX_WIDTH) {
//            args.setQuality(QUALITY - 1);
//        }
        args.setSelected(diagramResult.getEvents());
        args.setWriteTitle(false);
        args.setResource(resource);
        try {
            //            System.out.println("after : " + diagram.getWidth() + "x" + diagram.getHeight());
            return exporter.export(args, result);
        } catch (Exception pascual) {
            return null;
        }
    }


    public static SVGDocument getSVGDiagram(String stId, AnalysisStoredResult result) {
        DiagramResult diagramResult = DIAGRAM_SERVICE.getDiagramResult(stId);
        RasterArgs args = new RasterArgs(diagramResult.getDiagramStId(), "png");
        args.setSelected(diagramResult.getEvents());
        args.setWriteTitle(false);
        try {
            return exporter.exportToSvg(args, result);
        } catch (Exception pascual) {
            return null;
        }
    }

    public static void setPaths(ReportArgs reportArgs) {
        exporter = new RasterExporter(reportArgs.getDiagramPath(), reportArgs.getEhldPath(), reportArgs.getAnalysisPath(), reportArgs.getSvgSummary());
    }

    private static int getQuality(float factor) {
//        factor = factor > MAX_FACTOR ? MAX_FACTOR : factor;
        if (factor < 1) {
            return (int) Math.ceil(4 * (factor - 0.1) / 0.9 + 1);
        } else {
            return (int) (5 * (factor - 1) / 2 + 5);
        }
    }
}
