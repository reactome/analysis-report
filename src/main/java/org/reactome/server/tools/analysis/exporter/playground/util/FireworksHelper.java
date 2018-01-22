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
    private static final double QUALITY = 3.0;

    static {
        AnalysisClient.setServer(URL.REACTOME);
    }

    /**
     * get the buffered image from fireworks exporter by given token
     *
     * @param reportArgs token produced by the server when it complete the analysis with your data,pick it from the URL
     * @return {@see BufferedImage}.
     * @throws FailToGetFireworksException
     */
    public BufferedImage getFireworks(ReportArgs reportArgs) throws FailToGetFireworksException {
        FireworkArgs args = new FireworkArgs(SPECIES, "png");
        args.setFactor(QUALITY);
        args.setToken(reportArgs.getToken());
        args.setProfile(FireworksColor.COPPER_PLUS.getColor());
        /**
         * looks add flags will result in a lot of time consumption for to request info
         */
//        ContentServiceClient.setHost("https://reactome.org/");
//        args.setFlags(Arrays.asList("CTP"));

        try {
            return new FireworksExporter(args, reportArgs.getFireworksPath()).render();
        } catch (Exception pascual) {
            throw new FailToGetFireworksException(String.format("Failed to get fireworks for token : ", reportArgs.getToken()), pascual);
        }
    }
}
