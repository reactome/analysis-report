package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetDiagramException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {

    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final int QUALITY = 5;

    static {
        AnalysisClient.setServer(URL.REACTOME);
    }

    /**
     * create pathway's diagram.
     *
     * @param stId       stable identifier of the diagram.
     * @param reportArgs {@see ReportArgs}.
     * @return {@see BufferedImage}.
     * @throws FailToGetDiagramException
     */
    public BufferedImage getDiagram(String stId, ReportArgs reportArgs) throws FailToGetDiagramException {
        RasterArgs args = new RasterArgs(stId, "png");
        args.setQuality(QUALITY);
        // TODO: 22/01/18 add analysis info on diagram
//        args.setToken(reportArgs.getToken());
//        args.setFlags(Arrays.asList("PTEN"));
//        args.setProfiles(new ColorProfiles("standard", "modern", "modern"));
        try {
            return RasterExporter.export(args, reportArgs.getDiagramPath(), reportArgs.getEhldPath());
        } catch (Exception pascual) {
            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
        }
    }
}
