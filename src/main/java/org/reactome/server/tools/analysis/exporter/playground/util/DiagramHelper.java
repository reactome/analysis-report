package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonDeserializationException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonNotFoundException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.ehld.exception.EHLDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Help to create the diagram image by invoking the Reactome RasterExporter{@see RasterExporter}.
 *
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {
    private static RasterExporter exporter;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiagramHelper.class);

    /**
     * create diagram image by using the RasterExporter{@see RasterExporter}.
     *
     * @param stId stable identifier of the diagram.
     * @param asr  {@see AnalysisStoredResult} contains the diagram analysis overlay information.
     * @return diagram.
     */
    public static BufferedImage getPNGDiagram(String stId, AnalysisStoredResult asr, String resource) {
        DiagramResult diagramResult = GraphCoreHelper.getDiagramResult(stId);
        RasterArgs args = new RasterArgs(diagramResult.getDiagramStId(), "png");
        args.setSelected(diagramResult.getEvents());
        args.setWriteTitle(false);
        args.setResource(resource);
        try {
            return exporter.export(args, asr);
        } catch (DiagramJsonNotFoundException | AnalysisException | EHLDException | DiagramJsonDeserializationException e) {
            e.printStackTrace();
            LOGGER.error("Failed to create diagram for token: {}.", asr.getSummary().getToken());
            return null;
        }
    }

    /**
     * Set the necessary file before use it.
     *
     * @param reportArgs args contains the diagram/ehld/analysis path and svg summary file.
     */
    public static void setPaths(ReportArgs reportArgs) {
        exporter = new RasterExporter(reportArgs.getDiagramPath(), reportArgs.getEhldPath(), reportArgs.getAnalysisPath(), reportArgs.getSvgSummary());
    }
}
