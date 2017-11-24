//package com.example.demo.diagramexporter;
//
//import org.junit.Test;
//import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
//import org.reactome.server.tools.diagram.exporter.raster.api.SimpleRasterArgs;
//import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
//import org.springframework.boot.test.context.SpringBootTest;
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//
///**
// * @author Chuan Deng <cdeng@ebi.ac.uk>
// */
//@SpringBootTest
//public class DiagramExportDemoTests {
//    @Test
//    public void test() throws Exception{
//        // This path must contain "R-HSA-169911.json" and "R-HSA-169911.graph.json" files
//        String diagramPath = "src/main/resources/static";
//        String ehldPath = "src/main/resources/static";
//
//        final SimpleRasterArgs args = new SimpleRasterArgs("9903581", "jpg");
//        args.setFactor(2.);
////        args.setProfiles(new ColorProfiles("standard", null, null));
//        args.setBackground(new Color(0x36767D));
//
//        final BufferedImage image = RasterExporter.export(args, diagramPath, ehldPath);
//
//// If saving to a file
//        final File file = new File(args.getStId() + "." + args.getFormat());
//        ImageIO.write(image, args.getFormat(), file);
//
//    }
//}
