package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToLoadProfileException;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ReportRenderer {

    private static DataSet dataSet;
    private static PdfDocument document;
    private static AnalysisReport report;
    private static List<Section> sections;
    private static PdfProfile profile;
    private static final String profilePath = "src/main/resources/profile.json";
    private static final Logger logger = LoggerFactory.getLogger(ReportRenderer.class);

    static {
        //create a new thread to detect change of profile.json
        try {
            loadPdfProfile(profilePath);
//            new Thread(new profileWatcher()).run();
        } catch (Exception e) {
            logger.error("Failed to init pdf profile from config json file:{}", profilePath);
        }
    }

    protected static void render(ReportArgs reportArgs, PdfWriter writer) throws Exception {
        dataSet = PdfUtils.getDataSet("MjAxNzEyMTgwNjM0MDJfMjI%253D");
        dataSet.setReportArgs(reportArgs);
        document = new PdfDocument(writer);
        report = new AnalysisReport(profile, document);
        sections = new ArrayList<>();

        sections.add(new Footer());
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

//        sections.stream().forEach(section -> section.render(report,properties,dataSet));

        try {
            for (Section section : sections) {
                section.render(report, dataSet);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        } finally {
            report.close();
            document.close();
        }

    }

    synchronized protected static void loadPdfProfile(String profilePath) throws Exception {
        try {
            profile = new PdfProfile();
            JsonFactory jasonFactory = new JsonFactory();
            JsonParser jsonParser = jasonFactory.createParser(new File(profilePath));
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();

                //get the current token
                if ("margin".equals(fieldname)) {
                    jsonParser.nextToken();
                    profile.setMargin(jsonParser.getIntValue());
                }
                if ("numberOfPathwaysToShow".equals(fieldname)) {
                    jsonParser.nextToken();
                    profile.setNumberOfPathwaysToShow(jsonParser.getIntValue());
                }
                if ("multipliedLeading".equals(fieldname)) {
                    jsonParser.nextToken();
                    profile.setMultipliedLeading(jsonParser.getFloatValue());
                }
                if ("font".equals(fieldname)) {
                    jsonParser.nextToken();
                    profile.setFont(PdfFontFactory.createFont(jsonParser.getValueAsString()));
                }
                if ("titleColor".equals(fieldname)) {
                    //move to [
                    jsonParser.nextToken();
                    // loop till token equal to "]"
                    float[] colorValue = new float[3];
                    int index = 0;
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        colorValue[index++] = jsonParser.getFloatValue();
                        profile.setTitleColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                    }
                }
                if ("paragraphColor".equals(fieldname)) {
                    jsonParser.nextToken();
                    float[] colorValue = new float[3];
                    int index = 0;
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        colorValue[index++] = jsonParser.getFloatValue();
                        profile.setParagraphColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                    }
                }
                if ("tableColor".equals(fieldname)) {
                    jsonParser.nextToken();
                    float[] colorValue = new float[3];
                    int index = 0;
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        colorValue[index++] = jsonParser.getFloatValue();
                        profile.setTableColor(new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]));
                    }
                }
            }
            System.out.println(profile.toString());
        } catch (IOException e) {
            throw new FailToLoadProfileException(String.format("Fail to load profile file from:%s", profilePath), e);
        }
    }
}

class profileWatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(profileWatcher.class);

    @Override
    public void run() {
        try {
            final Path path = Paths.get("src/main/resources/");
            final WatchService watchService = path.getFileSystem().newWatchService();

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = watchService.take();
                if (watchKey != null) {
                    watchKey.pollEvents().stream().filter(event -> ((Path) event.context()).toFile().getName().equals("profile.json")).forEach(event ->
                    {
                        LOGGER.info("reload Config file");
                        try {
                            ReportRenderer.loadPdfProfile("src/main/resources/profile.json");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                }
                watchKey.reset();
            }
        } catch (Exception e) {
            LOGGER.error("Fail to reload Config file!");
        }
    }
}
