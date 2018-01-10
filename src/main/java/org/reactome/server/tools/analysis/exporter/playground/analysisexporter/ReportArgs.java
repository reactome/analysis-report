package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.reactome.server.tools.analysis.exporter.playground.util.HttpClientHelper;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * <p>
 * This class packaged the essential parameters to produce the analysis report.
 * and it will be an argument passed to {@see AnalysisExporter#export}
 */
public class ReportArgs {
    private String token;
    private String diagramPath;
    private String ehldPath;
    private String fireworksPath;

    /**
     * @param token         token produced by the {@see <a href="https://reactome.org">Reactome</a>} server end once the user submitted the data set to perform the analysis service.
     * @param diagramPath   the diagram raw json file path to export the pathway diagram image.
     * @param ehldPath      the EHLD raw json file path to export the pathway diagram image.
     * @param fireworksPath the fireworks raw json file path to export the analysis fireworks image.
     * @throws Exception when input token been checked to be invalid.
     */
    public ReportArgs(String token, String diagramPath, String ehldPath, String fireworksPath) throws Exception {
        this.token = HttpClientHelper.checkToken(token);
        this.diagramPath = diagramPath;
        this.ehldPath = ehldPath;
        this.fireworksPath = fireworksPath;
    }

    public String getToken() {
        return token;
    }

    public String getDiagramPath() {
        return diagramPath;
    }

    public String getEhldPath() {
        return ehldPath;
    }

    public String getFireworksPath() {
        return fireworksPath;
    }

}
