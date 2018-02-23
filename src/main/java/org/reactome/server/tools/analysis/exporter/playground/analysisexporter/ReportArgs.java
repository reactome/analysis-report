package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * <p>
 * This class packaged the essential parameters to produce the analysis report.
 * and it will be an argument passed to {@see AnalysisExporter#export}
 */
public class ReportArgs {

    private String token;
    private String resource = null;
    //    private Resource resource = new Resource("CHEBI");
//        private String resource = "CHEBI";
//    private String resource = "TOTAL";
    private Long species = null;
    private Integer pagination = 0;
    private String diagramPath;
    private String ehldPath;
    private String fireworksPath;
    private String analysisPath;
    private String svgSummary;

    /**
     * @param token         token produced by the {@see <a href="https://reactome.org">Reactome</a>} server end once the user submitted the data set to perform the analysis service.
     * @param diagramPath   the diagram raw json file path to export the pathway diagram image.
     * @param ehldPath      the EHLD raw json file path to export the pathway diagram image.
     * @param fireworksPath the fireworks raw json file path to export the analysis fireworks image.
     */
    public ReportArgs(String token, String diagramPath, String ehldPath, String fireworksPath, String analysisPath, String svgSummary) {
        this.token = token;
        this.diagramPath = diagramPath;
        this.ehldPath = ehldPath;
        this.fireworksPath = fireworksPath;
        this.analysisPath = analysisPath;
        this.svgSummary = svgSummary;
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

    public String getAnalysisPath() {
        return analysisPath;
    }

    public String getSvgSummary() {
        return svgSummary;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Long getSpecies() {
        return species;
    }

    public void setSpecies(Long species) {
        this.species = species;
    }

    public int getPagination() {
        return pagination;
    }

    public void setPagination(Integer pagination) {
        this.pagination = pagination;
    }
}
