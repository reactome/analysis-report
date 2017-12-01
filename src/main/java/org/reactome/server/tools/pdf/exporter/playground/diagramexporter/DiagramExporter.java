package org.reactome.server.tools.pdf.exporter.playground.diagramexporter;

import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.SimpleRasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.FailToGetDiagramException;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
public class DiagramExporter {

    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final String diagramPath = "/home/byron/static/demo";
    private static final String ehldPath = "/home/byron/static";
    private static final String diagramFormat = "png";

    private static final int quality = 10;

    public static BufferedImage getDiagram(String stId) throws FailToGetDiagramException {
        try {
            final SimpleRasterArgs args = new SimpleRasterArgs(stId, diagramFormat);
            args.setQuality(quality);
            args.setProfiles(new ColorProfiles("standard", null, null));
            return RasterExporter.export(args, diagramPath, ehldPath);
        } catch (Exception pascual) {
            throw new FailToGetDiagramException(String.format("Failed to get diagram stId:%s", stId), pascual);
        }
    }
}
