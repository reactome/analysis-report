package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetFireworksException;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.reactome.server.tools.fireworks.exporter.raster.FireworksExporter;

import java.awt.image.BufferedImage;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FireworksHelper {

    //this file must contain the *.json files,you down it from
    private static final String SPECIES = "Homo_sapiens";
    private static final String FIREWORKS_FORMAT = "png";
    private static final double QUALITY = 5.0;

    /**
     * get the buffered image from fireworks exporter by given token
     * @param reportArgs token produced by the server when it complete the analysis with your data,pick it from the URL
     * @return {@see BufferedImage}.
     * @throws FailToGetFireworksException
     */
    public static BufferedImage getFireworks(ReportArgs reportArgs) throws FailToGetFireworksException {
        try {
            AnalysisClient.setServer(URL.REACTOME);
            FireworkArgs args = new FireworkArgs(SPECIES, FIREWORKS_FORMAT);
            args.setFactor(QUALITY);
            args.setToken(reportArgs.getToken());
            args.setProfile(FireworksColor.COPPER_PLUS.getColor());
            FireworksExporter exporter = new FireworksExporter(args, reportArgs.getFireworksPath());
            return exporter.render();
        } catch (Exception pascual) {
            throw new FailToGetFireworksException(String.format("Failed to get fireworks for token : ", reportArgs.getToken()), pascual);
        }
    }
}
