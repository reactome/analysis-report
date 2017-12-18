package org.reactome.server.tools.analysis.exporter.playground.utils;

import org.reactome.server.tools.analysis.exporter.playground.exceptions.FailToGetDiagramException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
public class DiagramHelper {

    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final String diagramPath = "/home/byron/static/demo";
    private static final String ehldPath = "/home/byron/static";
    private static final String diagramFormat = "png";
    private static final int quality = 10;

    /**
     * @param stId      stable identifier of the diagram
     * @return BufferedImage
     * @throws FailToGetDiagramException fail to get diagram by stId
     */
    public static BufferedImage getDiagram(String stId) throws FailToGetDiagramException {
        try {
            final RasterArgs args = new RasterArgs(stId, diagramFormat);
            args.setQuality(quality);
            args.setProfiles(new ColorProfiles("standard", null, null));
            return RasterExporter.export(args, diagramPath, ehldPath);
        } catch (Exception pascual) {
            throw new FailToGetDiagramException("Failed to get diagram stId:" + stId, pascual);
        }
    }
}
