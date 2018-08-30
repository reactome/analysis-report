package org.reactome.server.tools.analysis.report;

import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the data needed for the analysis. All of the accesses to the graph
 * database and the AnalysisStoredResult are done here, providing a convenient
 * method {@link AnalysisData#getPathways()} to fetch data.
 */
public class AnalysisData {

	private static GeneralService genericService = ReactomeGraphCore.getService(GeneralService.class);
	private static DatabaseObjectService databaseObjectService = ReactomeGraphCore.getService(DatabaseObjectService.class);
	private final AnalysisType type;
	private final List<PathwayData> pathways;
	private final String beautifiedResource;
	private final String name;
	private final String species;
	private final String speciesComparisonSpecies;
	private final String resource;
	private final AnalysisStoredResult analysisStoredResult;
	private final Long speciesDbId;
	private final int maxPathways;
	private final boolean projection;
	private final boolean interactors;

	AnalysisData(AnalysisStoredResult analysisStoredResult, String resource, Long speciesDbId, int maxPathways) {
		this.analysisStoredResult = analysisStoredResult;
		this.resource = resource;
		this.speciesDbId = speciesDbId;
		this.beautifiedResource = beautify(resource);
		this.species = getSpeciesName(speciesDbId);
		this.type = AnalysisType.valueOf(analysisStoredResult.getSummary().getType());
		this.speciesComparisonSpecies = getSpeciesName(analysisStoredResult.getSummary().getSpecies());
		this.maxPathways = maxPathways;
		this.name = computeName();
		this.projection = analysisStoredResult.getSummary().isProjection() != null && analysisStoredResult.getSummary().isProjection();
		this.interactors = analysisStoredResult.getSummary().isInteractors() != null && analysisStoredResult.getSummary().isInteractors();
		pathways = collectPathways();
	}

	/**
	 * @return Reactome's current database version.
	 */
	public static int getDBVersion() {
		return genericService.getDBVersion();
	}

	private static String getSpeciesName(Long id) {
		if (id == null) return null;
		Species species = databaseObjectService.findByIdNoRelations(id);
		return species.getName().get(0);
	}

	private List<PathwayData> collectPathways() {
		return analysisStoredResult.filterBySpecies(speciesDbId, resource).getPathways().stream()
				.limit(maxPathways)
				.map(base -> {
					final PathwayNodeSummary summary = analysisStoredResult.getPathway(base.getStId());
					final Pathway pathway = databaseObjectService.findByIdNoRelations(base.getStId());
					return (new PathwayData(summary, base, pathway));
				})
				.collect(Collectors.toList());
	}

	private String computeName() {
		for (String alternative : new String[]{
				analysisStoredResult.getSummary().getSampleName(),
				analysisStoredResult.getSummary().getFileName(),
				speciesComparisonSpecies,
				analysisStoredResult.getSummary().getType(),
				analysisStoredResult.getSummary().getToken()}) {
			if (alternative != null) return alternative;
		}
		return "";
	}

	public String beautify(String resource) {
		switch (resource.toLowerCase()) {
			case "uniprot":
				return "UniProt";
			case "chebi":
				return "ChEBI";
			case "ensembl":
				return "Ensembl";
			case "gene":
			case "compound":
				return "KEGG";
			case "pubmed":
				return "PubMed";
			case "total":
				return "all resources";
			default:
				return resource;
		}
	}

	public AnalysisStoredResult getAnalysisStoredResult() {
		return analysisStoredResult;
	}

	public Collection<PathwayData> getPathways() {
		return pathways;
	}

	public String getName() {
		return name;
	}

	public AnalysisType getType() {
		return type;
	}

	public String getSpeciesComparisonSpeciesName() {
		return speciesComparisonSpecies;
	}

	public String getSpecies() {
		return species;
	}

	public String getResource() {
		return resource;
	}

	public String getBeautifiedResource() {
		return beautifiedResource;
	}

	public boolean isInteractors() {
		return interactors;
	}

	public boolean isProjection() {
		return projection;
	}
}
