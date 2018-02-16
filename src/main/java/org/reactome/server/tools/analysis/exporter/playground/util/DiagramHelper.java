package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagramHelper.class);
    private static final int QUALITY = 5;
    private static RasterExporter exporter;


    /**
     * create pathway's diagram.
     *
     * @param stId   stable identifier of the diagram.
     * @param result {@see AnalysisStoredResult}
     * @return {@see BufferedImage}.
     */
    public static BufferedImage getPNGDiagram(String stId, AnalysisStoredResult result) {
        RasterArgs args = new RasterArgs(stId, "png");
        args.setQuality(QUALITY);
        args.setWriteTitle(false);
        try {
            return exporter.export(args, result);
        } catch (Exception pascual) {
//            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
//            LOGGER.warn("Failed to create image for stId {}.",stId);
            return null;
        }
    }

    public static SVGDocument getSVGDiagram(String stId, AnalysisStoredResult result) {
        RasterArgs args = new RasterArgs(stId, "png");
        args.setQuality(QUALITY);
        args.setWriteTitle(false);
        try {
            return exporter.exportToSvg(args, result);
        } catch (Exception pascual) {
//            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
//            LOGGER.warn("Failed to create image for stId {}.",stId);
            return null;
        }
    }

    public static void setPaths(String diagramPath, String ehldPath, String analysisPath) {
        final String svgSummary = Paths.get(ehldPath, "svgSummary.txt").toFile().getAbsolutePath();
        exporter = new RasterExporter(diagramPath, ehldPath, analysisPath, svgSummary);

    }
}
