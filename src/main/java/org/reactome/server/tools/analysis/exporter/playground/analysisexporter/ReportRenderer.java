package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
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

    private static final String PROFILE_PATH = "src/main/resources/";
    private static final String PROFILE_NAME = "profile.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
    private static PdfProfile profile = new PdfProfile();

    /**
     * to load the PDF profile configuration file before create the document.
     */
    static {
        try {
            loadPdfProfile(PROFILE_PATH + PROFILE_NAME);
//            ProfileWatcher watcher = new ProfileWatcher("profileWatcher", PROFILE_PATH, PROFILE_NAME);
//            watcher.start();
        } catch (Exception e) {
            LOGGER.warn("Failed to init pdf profile correctly from : {}{}", PROFILE_PATH, PROFILE_NAME);
        }
    }

    /**
     * this method is to render the report with data set.
     *
     * @param reportArgs  {@see ReportArgs}
     * @param destination {@see PdfWriter}
     * @throws Exception when fail to create the PDF document.
     */
    protected static void render(ReportArgs reportArgs, FileOutputStream destination) throws Exception {
        List<Section> sections = new ArrayList<>(6);
        DataSet dataSet = PdfUtils.getDataSet(reportArgs, profile.getNumberOfPathwaysToShow());
        // TODO: 19/01/18 use FileOutputStream
        dataSet.setFile(destination);
        // TODO: 19/01/18 switch off smart model
//        PdfWriter writer = new PdfWriter(destination, new WriterProperties().setFullCompressionMode(true)).setSmartMode(false);
        PdfWriter writer = new PdfWriter(destination).setSmartMode(false);
        PdfDocument document = new PdfDocument(writer);
        document.setFlushUnusedObjects(true);
        AnalysisReport report = new AnalysisReport(document, profile, dataSet);

        sections.add(new Footer());
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

        try {
            for (Section section : sections) {
                section.render(report);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        } finally {
            report.getDataSet().release();
            report.close();
            document.close();
            writer.close();
        }
    }

    /**
     * load the {@see PdfProfile} config information to control PDF layout.
     *
     * @param profilePath path contains profile.json config file.
     * @throws IOException
     */
    protected static void loadPdfProfile(String profilePath) {
        synchronized (profile) {
            try {
                profile = MAPPER.readValue(new File(profilePath), PdfProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info(profile.toString());
    }
}