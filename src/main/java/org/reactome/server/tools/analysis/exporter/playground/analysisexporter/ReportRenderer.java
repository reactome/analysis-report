package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domain.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
class ReportRenderer {

    private static final String PROFILE_PATH = "src/main/resources/";
    private static final String PROFILE_NAME = "profile.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
    private static PdfProfile profile = new PdfProfile();

    /**
     * to load the PDF profile configuration file before create the document.
     */
    static {
        try {
            loadPdfProfile(PROFILE_PATH + PROFILE_NAME);
            ProfileWatcher watcher = new ProfileWatcher("profileWatcher", PROFILE_PATH, PROFILE_NAME);
            watcher.start();
        } catch (Exception e) {
            LOGGER.warn("Failed to init pdf profile correctly from : {}{}", PROFILE_PATH, PROFILE_NAME);
        }
    }

    /**
     * this method is to render the report with data set.
     *
     * @param reportArgs {@see ReportArgs}
     * @param file       {@see PdfWriter}
     * @throws Exception when fail to create the PDF document.
     */
    protected static void render(ReportArgs reportArgs, OutputStream file) throws Exception {
        List<Section> sections = new ArrayList<>(6);
        DataSet dataSet = PdfUtils.getDataSet(reportArgs, profile.getNumberOfPathwaysToShow());
        // TODO: 19/01/18 use fileoutputstream
        dataSet.setFile((FileOutputStream) file);
        // TODO: 19/01/18 use smart model
        PdfWriter writer = new PdfWriter(file, new WriterProperties().setFullCompressionMode(true)).setSmartMode(false);
        PdfDocument document = new PdfDocument(writer);
        document.setFlushUnusedObjects(true);
        AnalysisReport report = new AnalysisReport(document, profile);

        sections.add(new Footer());
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

        try {
            for (Section section : sections) {
                section.render(report, dataSet);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        } finally {
            sections = null;
            dataSet.release();
            if (report != null) report.close();
            if (document != null) document.close();
            report = null;
            document = null;
            writer = null;
        }
    }

    /**
     * load the {@see PdfProfile} config information to control PDF layout.
     *
     * @param profilePath path contains profile.json config file.
     * @throws IOException
     */
    protected static void loadPdfProfile(String profilePath) throws IOException {
        synchronized (profile) {
            profile = PdfUtils.readValue(new File(profilePath), PdfProfile.class);
        }
        LOGGER.info(profile.toString());
    }
}