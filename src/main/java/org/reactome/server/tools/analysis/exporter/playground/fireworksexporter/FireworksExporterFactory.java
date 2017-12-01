package org.reactome.server.tools.analysis.exporter.playground.fireworksexporter;

import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.exceptions.FailToGetFireworksException;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisException;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.reactome.server.tools.fireworks.exporter.raster.FireworksExporter;

import java.awt.image.BufferedImage;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FireworksExporterFactory {
    private static final String fireworksPath = "/home/byron/json";
    private static final String species = "Homo_sapiens";
    private static final String fireworksFormat = "png";
    private static final double quality = 5.0;


    public static BufferedImage getFireworks(String token) throws FailToGetFireworksException {
        try {
            AnalysisClient.setServer(URL.REACTOME);
            FireworkArgs args = new FireworkArgs(species, fireworksFormat);
            args.setFactor(quality);
            args.setToken(token);
            args.setProfile(FireworksColor.CalciumSalts.getColor());
            FireworksExporter exporter = new FireworksExporter(args, fireworksPath);
            return exporter.render();
        } catch (AnalysisException | AnalysisServerError pascual) {
            throw new FailToGetFireworksException("Failed to get fireworks for token:" + token, pascual);
        }
    }
}
