package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
class ReportRenderer {

    private static final String PROFILE = "src/main/resources/profile.json";
    private static final String LINKICON = "src/main/resources/images/link.png";
    private static final String pathDirectory = "src/test/resources/analysis";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
    private static final TokenUtils tokenUtils = new TokenUtils(pathDirectory);
    private static PdfProfile profile = new PdfProfile();

    /**
     * this method is to render the report with data set.
     *
     * @param reportArgs  {@see ReportArgs}
     * @param destination {@see PdfWriter}
     * @throws Exception when fail to create the PDF document.
     */
    protected static void render(ReportArgs reportArgs, FileOutputStream destination) throws Exception {
        DiagramHelper.setPaths(reportArgs.getDiagramPath(), reportArgs.getEhldPath(), reportArgs.getAnalysisPath());
        FireworksHelper.setPaths(reportArgs.getFireworksPath(), reportArgs.getAnalysisPath());

        AnalysisStoredResult result = tokenUtils.getFromToken(reportArgs.getToken());
        loadPdfProfile(PROFILE);

        AnalysisReport report = new AnalysisReport(profile, destination);
        report.setLinkIcon(PdfUtils.createImage(LINKICON));
        List<Section> sections = new ArrayList<>(6);
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

        try {
            for (Section section : sections) {
                section.render(report, result);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        } finally {
            report.close();
            report.getPdfDocument().close();
        }
    }

    /**
     * load the {@see PdfProfile} config information to control PDF layout.
     *
     * @param profilePath path contains profile.json config file.
     */
    protected static void loadPdfProfile(String profilePath) {
        synchronized (profile) {
            try {
                profile = MAPPER.readValue(new File(profilePath), PdfProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        LOGGER.info(profile.toString());
    }
}