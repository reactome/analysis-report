package org.reactome.server.tools.analysis.exporter.playground.utils;

import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.exceptions.FailToGetFireworksException;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.reactome.server.tools.fireworks.exporter.raster.FireworksExporter;

import java.awt.image.BufferedImage;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FireworksHelper {

    //this file must contain the *.json files,you down it from
    private static final String fireworksPath = "/home/byron/json";
    private static final String species = "Homo_sapiens";
    private static final String fireworksFormat = "png";
    private static final double quality = 5.0;

    /**
     *<P>
     *     this method is to get the buffered image from fireworks exporter by give the token
     *</P>
     * @param token token produced by the server when it complete the analysis with your data,pick it from the URL
     * @return BufferedImage
     * @throws FailToGetFireworksException
     */
    public static BufferedImage getFireworks(String token) throws FailToGetFireworksException {
        try {
            AnalysisClient.setServer(URL.REACTOME);
            FireworkArgs args = new FireworkArgs(species, fireworksFormat);
            args.setFactor(quality);
            args.setToken(token);
            args.setProfile(FireworksColor.CopperPlus.getColor());
            FireworksExporter exporter = new FireworksExporter(args, fireworksPath);
            return exporter.render();
        } catch (Exception pascual) {
            throw new FailToGetFireworksException("Failed to get fireworks for token:" + token, pascual);
        }
    }
}
