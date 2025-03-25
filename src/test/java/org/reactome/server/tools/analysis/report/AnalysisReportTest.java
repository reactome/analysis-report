package org.reactome.server.tools.analysis.report;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reactome.server.graph.config.GraphCoreNeo4jConfig;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.report.util.AnalysisReportGraphConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * @author Lorente Pascual plorente@ebi.ac.uk
 */
@SpringBootTest
@ContextConfiguration(classes = {GraphCoreNeo4jConfig.class})
public class AnalysisReportTest {

    /*
     * NOTE: for testing
     * Testing the creation of a PDF requires the use of external resources. Although we've tried to reduce the amount
     * of these resources, some configuration is needed.
     *   1) Graph database: a live connection to Reactome graph database v65 is required. We use already performed
     *      analysis to test this project. These analyses have been run against Reactome graph database version 65. A
     *      live connection is needed by specifying through System properties
     *              - neo4j.host
     *              - neo4j.port
     *              - neo4j.user
     *              - neo4j.password
     *   2) Diagrams: paths to diagrams and fireworks are required. We tried to get them under test/resources, but every
     *      time tests are changed, these resources have to be changed as well. To avoid this problem, and for a more
     *      dynamic testing, we decided to externalize them. Use the following System properties:
     *              - diagram.path
     *              - fireworks.path
     *              - ehld.path
     *              - svg.summary
     */

    private static final Map<String, String> tokens = Map.of(
            "PTEN", "MjAyNTAyMjExMDQwMTVfMjUxMQ%253D%253D"
//			"EXP1", "MjAxODEwMDQxMDA4MDNfNA%253D%253D"
//			"SPECIES1", "MjAxODEwMDQxMDA4MzVfNQ%253D%253D"
    );


    private static AnalysisReport RENDERER;


    @BeforeAll
    public static void beforeClass(
            @Value("${diagram.folder}") String diagramPath,
            @Value("${fireworks.folder}") String fireworksPath,
            @Value("${ehld.folder}") String ehldPath,
            @Value("${svg.summary.path}") String svgSummaryPath,
            @Value("${spring.neo4j.uri}") String neo4jUri,
            @Value("${spring.neo4j.authentication.username}") String neo4jUsername,
            @Value("${spring.neo4j.authentication.password}") String neo4jPassword,
            @Value("${spring.data.neo4j.database}") String neo4jDatabase
    ) {
        String analysisPath = "src/test/resources/org/reactome/server/tools/analysis/report/analysis";
        ReactomeGraphCore.initialise(neo4jUri, neo4jUsername, neo4jPassword, neo4jDatabase, AnalysisReportGraphConfig.class);
        RENDERER = new AnalysisReport(diagramPath, ehldPath, fireworksPath, analysisPath, svgSummaryPath);
    }

    @Test
    public void exportTest() {
        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            final String type = entry.getKey();
            final String token = entry.getValue();
            LoggerFactory.getLogger(AnalysisReportTest.class).info(type);
            try {
                final OutputStream os = new FileOutputStream("output.pdf");
                RENDERER.create(token, "TOTAL", 48887L, 10, false, "modern", "copper plus", "copper", os);
            } catch (AnalysisExporterException e) {
                e.printStackTrace();
                Assertions.fail(e.getMessage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
