package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

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
     */
    public ReportArgs(String token, String diagramPath, String ehldPath, String fireworksPath) {
        this.token = token;
        this.diagramPath = diagramPath;
        this.ehldPath = ehldPath;
        this.fireworksPath = fireworksPath;
    }

    public String getToken() {
        return token;
    }

    public ReportArgs setToken(String token) {
        this.token = token;
        return this;
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
