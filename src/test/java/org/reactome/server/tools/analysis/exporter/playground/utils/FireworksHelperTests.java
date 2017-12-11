package org.reactome.server.tools.analysis.exporter.playground.utils;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FireworksHelperTests {
    @Test
    public void test() throws Exception {
        String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
        BufferedImage fireworks = FireworksHelper.getFireworks(token);
        File file = new File("src/main/resources/fireworks/", "Homo_sapiens.png");
        ImageIO.write(fireworks, "png", file);

//        FireworkArgs args = new FireworkArgs("Homo_sapiens", "gif");
//        args.setProfile("Copper plus");
//        args.setFactor(3.0);
//
//        AnalysisClient.setServer("https://reactome.org");
//        args.setToken("MjAxNzExMTcwODEzMjBfNzU%253D");
//        FireworksExporter exporter = new FireworksExporter(args, "/home/byron/json");
//
//        File file = new File("src/main/resources/fireworks/", "Homo_sapiens.gif");
//        FileOutputStream outputStream = new FileOutputStream(file);
//        exporter.renderToGif(outputStream);
    }
}
