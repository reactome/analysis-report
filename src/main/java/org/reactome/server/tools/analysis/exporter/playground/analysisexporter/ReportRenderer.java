package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.exception.InvalidPropertyException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ReportRenderer {

    private static final PdfProfile PDF_PROFILE = new PdfProfile();
    private static final String PROFILE_PATH = "src/main/resources/";
    private static final String PROFILE_NAME = "profile.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
    private static DataSet dataSet = null;
    private static PdfDocument document = null;
    private static AnalysisReport report = null;
    private static List<Section> sections = null;

    static {
        try {
            loadPdfProfile(PROFILE_PATH + PROFILE_NAME);
            ProfileWatcher watcher = new ProfileWatcher("profile watcher", PROFILE_PATH, PROFILE_NAME);
            watcher.start();
        } catch (Exception e) {
            LOGGER.error("Failed to init pdf profile correctly from : {}{}", PROFILE_PATH, PROFILE_NAME);
        }
    }

    protected static void render(ReportArgs reportArgs, PdfWriter writer) throws Exception {
        dataSet = PdfUtils.getDataSet(reportArgs.getToken());
        dataSet.setReportArgs(reportArgs);
        document = new PdfDocument(writer);
        report = new AnalysisReport(PDF_PROFILE, document);
        sections = new ArrayList<>();

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
            report.close();
            document.close();
            // to avoid exception "Pdf indirect object belongs to other PDF document. Copy object to current pdf document."
            // must create new font for very new AnalysisReport object.
            PDF_PROFILE.setFont(PdfFontFactory.createFont(PDF_PROFILE.getFontName()));
        }
    }

    protected static void loadPdfProfile(String profilePath) throws Exception {
        JsonFactory jasonFactory = new JsonFactory();
        JsonParser jsonParser = jasonFactory.createParser(new File(profilePath));
        float[] colorValue = new float[3];
        int index = 0;
        String fieldName;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            fieldName = jsonParser.getCurrentName();
            //get the current token
            if (fieldName == null)
                continue;
            synchronized (PDF_PROFILE) {
                switch (fieldName) {
                    case "margin":
                        jsonParser.nextToken();
                        PDF_PROFILE.setMargin(jsonParser.getIntValue());
                        break;
                    case "numberOfPathwaysToShow":
                        jsonParser.nextToken();
                        PDF_PROFILE.setNumberOfPathwaysToShow(jsonParser.getIntValue());
                        break;
                    case "multipliedLeading":
                        jsonParser.nextToken();
                        PDF_PROFILE.setMultipliedLeading(jsonParser.getFloatValue());
                        break;
                    case "font":
                        jsonParser.nextToken();
                        PDF_PROFILE.setFont(PdfFontFactory.createFont(jsonParser.getValueAsString()));
                        PDF_PROFILE.setFontName(jsonParser.getText());
                        break;
                    case "pageSize":
                        jsonParser.nextToken();
                        PDF_PROFILE.setPageSize(PdfUtils.createPageSize(jsonParser.getText()));
                        break;
                    case "titleColor":
                        // move to [
                        jsonParser.nextToken();
                        //loop till token equal to ']'
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            colorValue[index++] = jsonParser.getFloatValue();
                            PDF_PROFILE.setTitleColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                        }
                        index = 0;
                        break;
                    case "paragraphColor":
                        jsonParser.nextToken();
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            colorValue[index++] = jsonParser.getFloatValue();
                            PDF_PROFILE.setParagraphColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                        }
                        index = 0;
                        break;
                    case "tableColor":
                        jsonParser.nextToken();
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            colorValue[index++] = jsonParser.getFloatValue();
                            PDF_PROFILE.setTableColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                        }
                        index = 0;
                        break;
                    default:
                        LOGGER.warn("Cant recognize property : {}", fieldName);
                        throw new InvalidPropertyException(String.format("Cant recognize property : %s", fieldName));
                }
            }
        }
        LOGGER.info("profile was load : {}", PDF_PROFILE.toString());
    }
}

class ProfileWatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileWatcher.class);
    private String profilePath;
    private String profileName;
    private Thread thread;

    public ProfileWatcher(String threadName, String profilePath, String profileName) {
        thread = new Thread(this::run, threadName);
        this.profilePath = profilePath;
        this.profileName = profileName;
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        try {
            final Path path = Paths.get(profilePath);
            final WatchService watchService = path.getFileSystem().newWatchService();

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = watchService.take();
                if (watchKey != null) {
                    watchKey.pollEvents().stream().filter(event -> ((Path) event.context()).endsWith(profileName)).forEach(event ->
                    {
                        LOGGER.info("Reload {}", profilePath + profileName);
                        try {
                            ReportRenderer.loadPdfProfile(profilePath + profileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                watchKey.reset();
            }
        } catch (Exception e) {
            LOGGER.error("Fail to reload profile!");
        }
    }
}