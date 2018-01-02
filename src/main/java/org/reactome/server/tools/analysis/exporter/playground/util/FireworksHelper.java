package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetFireworksException;
import org.reactome.server.tools.fireworks.exporter.common.analysis.AnalysisClient;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.reactome.server.tools.fireworks.exporter.raster.FireworksExporter;

import java.awt.image.BufferedImage;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public abstract class FireworksHelper {

    //this file must contain the *.json files,you down it from
    private static final String species = "Homo_sapiens";
    private static final String fireworksFormat = "png";
    private static final double quality = 5.0;


    /**
     * <p>
     * this method is to get the buffered image from fireworks exporter by give the token
     * </P>
     *
     * @param reportArgs token produced by the server when it complete the analysis with your data,pick it from the URL
     * @return BufferedImage
     * @throws FailToGetFireworksException
     */
    public static BufferedImage getFireworks(ReportArgs reportArgs) throws FailToGetFireworksException {
        try {
            AnalysisClient.setServer(URL.REACTOME);
            FireworkArgs args = new FireworkArgs(species, fireworksFormat);
            args.setFactor(quality);
            args.setToken(reportArgs.getToken());
            args.setProfile(FireworksColor.CopperPlus.getColor());
            FireworksExporter exporter = new FireworksExporter(args, reportArgs.getFireworksPath());
            return exporter.render();
        } catch (Exception pascual) {
            throw new FailToGetFireworksException("Failed to get fireworks for token:" + reportArgs.getToken(), pascual);
        }
    }
}
