package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetDiagramException;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisClient;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.time.Instant;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagramHelper.class);
    private static final int QUALITY = 5;
    private static long total = 0;
    private static int count;

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
//    @Monitor(name = "getDiagram")
    public static BufferedImage getDiagram(String stId, ReportArgs reportArgs) {
        long start = Instant.now().toEpochMilli();
        RasterArgs args = new RasterArgs(stId, "png");
        args.setQuality(QUALITY);
        args.setWriteTitle(false);
//        args.setToken(reportArgs.getToken());
//        args.setFlags(Arrays.asList("PTEN"));
//        args.setProfiles(new ColorProfiles("standard", "modern", "modern"));
        try {
            BufferedImage diagram = RasterExporter.export(args, reportArgs.getDiagramPath(), reportArgs.getEhldPath());
            total += (Instant.now().toEpochMilli() - start);
            count++;
            return diagram;
        } catch (Exception pascual) {
//            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
//            LOGGER.warn("Failed to create image for stId {}.",stId);
            return null;
        }
    }

    public static long getTotal() {
        return total;
    }

    public static int getCount() {
        return count;
    }

    public static void reSet() {
        total = 0;
        count = 0;
    }
}
