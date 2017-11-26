package org.reactome.server.tools.pdf.exporter.playground.diagramexporter;

import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.SimpleRasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.reactome.server.tools.pdf.exporter.playground.exceptions.DiagramNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
public class DiagramExporter {
    private static final Logger logger = LoggerFactory.getLogger(DiagramExporter.class);
    // This path must contain "{stId}.json" and "{stId}.graph.json" files
    private static final String diagramPath = "/home/byron/static";
    private static final String ehldPath = "/home/byron/static";

    public static BufferedImage getBufferedImage(String stId) throws DiagramNotFoundException {
        try {
            final SimpleRasterArgs args = new SimpleRasterArgs(stId, "png");
            args.setQuality(3);
            args.setProfiles(new ColorProfiles("standard", null, null));

//            // If saving to a file
//            final File file = new File(args.getStId() + "." + args.getFormat());
//            ImageIO.write(image, args.getFormat(), file);
//            // If sending through an URL
//            URL url = new URL("...");
//            HttpUrlConnection connection = (HttpUrlConnection) url.openConnection();
//            connection.setDoOutput(true);  // your url must support writing
//            ImageIO.write(image, args.getFormat(), connection.getOutputStream());

            return RasterExporter.export(args, diagramPath, ehldPath);

        } catch (Exception e) {
            logger.error(String.format("Could not get diagram by pathway stId:%s", stId));
            throw new DiagramNotFoundException(String.format("Could not get diagram by pathway stId:%s", stId));
        }
    }
}
