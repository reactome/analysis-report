package org.reactome.server.tools.analysis.exporter.playground.fireworksexporter;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@SpringBootTest
public class FireworksExporterFactoryTests {
    @Test
    public void test() throws Exception {
        String token = "MjAxNzExMTcwNjQzNTRfMzE%253D";
        BufferedImage fireworks = FireworksExporterFactory.getFireworks(token);
        File file = new File("src/main/resources/fireworks/", "Homo_sapiens.png");
        ImageIO.write(fireworks, "png", file);
    }
}
