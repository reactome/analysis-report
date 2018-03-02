package org.reactome.server.tools.analysis.exporter;

/**
 * Essential parameters to perform the analysis report. and it will be an
 * argument passed to {@link AnalysisExporter#export}
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ReportArgs {

	private String token;
	private String resource = null;
	private Long species = null;
	private Integer pagination = 0;
	private String diagramPath;
	private String ehldPath;
	private String fireworksPath;
	private String analysisPath;
	private String svgSummary;

	/**
	 * @param token         token produced by the <a href="https://reactome.org/PathwayBrowser/#TOOL=AT">Analysis
	 *                      Tool</a>server end.
	 * @param diagramPath   path contains the diagram json data file.
	 * @param ehldPath      path contains the ehld json data file.
	 * @param fireworksPath path contains the fireworks json data file.
	 * @param analysisPath  path contains the binary file produced by analysis
	 *                      tool.
	 * @param svgSummary    file contains the information that if a pathway has
	 *                      the ehld image.
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
