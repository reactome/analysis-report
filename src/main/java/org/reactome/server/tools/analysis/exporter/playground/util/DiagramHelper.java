package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetDiagramException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;

import java.awt.image.BufferedImage;
import java.time.Instant;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {

    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final int QUALITY = 5;
    private static long total;
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
    public static BufferedImage getDiagram(String stId, ReportArgs reportArgs) throws FailToGetDiagramException {
        long start = Instant.now().toEpochMilli();
        RasterArgs args = new RasterArgs(stId, "png");
        args.setQuality(QUALITY);
        // TODO: 22/01/18 add analysis info on diagram
//        args.setToken(reportArgs.getToken());
//        args.setFlags(Arrays.asList("PTEN"));
//        args.setProfiles(new ColorProfiles("standard", "modern", "modern"));
        try {
            BufferedImage diagram = RasterExporter.export(args, reportArgs.getDiagramPath(), reportArgs.getEhldPath());
            total += (Instant.now().toEpochMilli() - start);
            count++;
            return diagram;
        } catch (Exception pascual) {
            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
        }
    }

    public static long getTotal() {
        return total;
    }

    public static int getCount() {
        return count;
    }

    public static void reSet() {
        DiagramHelper.total = 0;
        DiagramHelper.count = 0;
    }
}
