package org.reactome.server.tools.pdf.exporter.playground.diagramexporter;

import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.SimpleRasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.DiagramNotFoundException;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
public class DiagramExporter {

    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final String diagramPath = "/home/byron/static/demo";
    private static final String ehldPath = "/home/byron/static";

    public static BufferedImage getBufferedImage(String stId) throws DiagramNotFoundException {
        try {
            final SimpleRasterArgs args = new SimpleRasterArgs(stId, "png");
            args.setQuality(10);
            args.setProfiles(new ColorProfiles("standard", null, null));
            return RasterExporter.export(args, diagramPath, ehldPath);
        } catch (Exception e) {
            throw new DiagramNotFoundException(String.format("Diagram not found for pathway/reaction stId:%s", stId));
        }
    }
}
