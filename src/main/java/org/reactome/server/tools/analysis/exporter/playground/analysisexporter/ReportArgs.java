package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.reactome.server.tools.analysis.exporter.playground.util.HttpClientHelper;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ReportArgs {
    private String token;
    private String diagramPath;
    private String ehldPath;
    private String fireworksPath;

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
