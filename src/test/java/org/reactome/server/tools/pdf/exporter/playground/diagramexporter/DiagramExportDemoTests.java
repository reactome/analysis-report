package org.reactome.server.tools.pdf.exporter.playground.diagramexporter;

import org.junit.Test;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.SimpleRasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@SpringBootTest
public class DiagramExportDemoTests {

    @Test
    public void test() throws Exception {
        final Logger logger = LoggerFactory.getLogger(DiagramExporter.class);
        // This path must contain "{stId}.json" and "{stId}.graph.json" files
        final String diagramPath = "/home/byron/static/demo";
        final String ehldPath = "/home/byron/static";

        final SimpleRasterArgs args = new SimpleRasterArgs("R-HSA-6781827", "png");
        args.setQuality(7);
//        args.setToken("MjAxNzExMTcwODUzNTRfOTU=");
        args.setProfiles(new ColorProfiles("standard", null, null));

        final BufferedImage image = RasterExporter.export(args, diagramPath, ehldPath);
        // If saving to a file
        final File file = new File("src/main/resources/diagrams/"+args.getStId() + "." + args.getFormat());
        ImageIO.write(image, args.getFormat(), file);

//        return RasterExporter.export(args, diagramPath, ehldPath);
//        logger.error(String.format("Could not get diagram by pathway stId:%s", stId));

    }
}
