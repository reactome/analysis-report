package org.reactome.server.tools.analysis.exporter;

import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exporter.util.GraphCoreHelper;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class AnalysisData {

	private static final GeneralService generalService = ReactomeGraphCore.getService(GeneralService.class);
	private static final int MAX_PATHWAYS = 35;
	private final DatabaseObjectService databaseObjectService = ReactomeGraphCore.getService(DatabaseObjectService.class);
	private final AnalysisType type;
	private final Map<String, PathwayData> pathwayDataMap;
	private final String beautifiedResource;
	private final String name;
	private final String species;
	private final String speciesComparisonSpecies;
	private final String resource;
	private AnalysisStoredResult analysisStoredResult;
	private SpeciesFilteredResult speciesFilteredResult;

	AnalysisData(AnalysisStoredResult analysisStoredResult, String resource, Long speciesDbId) {
		this.analysisStoredResult = analysisStoredResult;
		this.resource = resource;
		this.beautifiedResource = beautify(resource);
		this.species = GraphCoreHelper.getSpeciesName(speciesDbId);
		this.type = AnalysisType.valueOf(analysisStoredResult.getSummary().getType());
		this.speciesFilteredResult = analysisStoredResult.filterBySpecies(speciesDbId, resource);
		this.speciesComparisonSpecies = GraphCoreHelper.getSpeciesName(analysisStoredResult.getSummary().getSpecies());
		this.name = computeName();
		pathwayDataMap = indexPathways();
	}

	private Map<String, PathwayData> indexPathways() {
		final Map<String, PathwayData> map = new LinkedHashMap<>();
		speciesFilteredResult.getPathways().stream().limit(MAX_PATHWAYS).forEach(pathwayBase -> {
			final PathwayNodeSummary pathwaySummary = analysisStoredResult.getPathway(pathwayBase.getStId());
			final Pathway pathway = databaseObjectService.findById(pathwayBase.getStId());
			map.put(pathway.getStId(), new PathwayData(pathwaySummary, pathwayBase, pathway));
		});
		return map;
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
				return "any resource";
			default:
				return resource;
		}
	}

	public AnalysisStoredResult getAnalysisStoredResult() {
		return analysisStoredResult;
	}

	public SpeciesFilteredResult getSpeciesFilteredResult() {
		return speciesFilteredResult;
	}


	public Collection<PathwayData> getPathways() {
		return pathwayDataMap.values();
	}

	public PathwayData getPathwayData(String stId) {
		return pathwayDataMap.get(stId);
	}

	public String getName() {
		return name;
	}

	public Integer getDBVersion() {
		return generalService.getDBVersion();
	}

	public AnalysisType getType() {
		return type;
	}

	public String getSpeciesComparisonSpecies() {
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
}
