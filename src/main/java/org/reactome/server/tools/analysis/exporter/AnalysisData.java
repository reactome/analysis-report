package org.reactome.server.tools.analysis.exporter;

import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;
import org.reactome.server.tools.analysis.exporter.util.GraphCoreHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class AnalysisData {

	private final AnalysisType type;
	private final List<PathwayData> pathways;
	private final String beautifiedResource;
	private final String name;
	private final String species;
	private final String speciesComparisonSpecies;
	private final String resource;
	private final AnalysisStoredResult analysisStoredResult;
	private final Long speciesDbId;

	AnalysisData(AnalysisStoredResult analysisStoredResult, String resource, Long speciesDbId) {
		this.analysisStoredResult = analysisStoredResult;
		this.resource = resource;
		this.speciesDbId = speciesDbId;
		this.beautifiedResource = beautify(resource);
		this.species = GraphCoreHelper.getSpeciesName(speciesDbId);
		this.type = AnalysisType.valueOf(analysisStoredResult.getSummary().getType());
		this.speciesComparisonSpecies = GraphCoreHelper.getSpeciesName(analysisStoredResult.getSummary().getSpecies());
		this.name = computeName();
		pathways = collectPathways();
	}

	private List<PathwayData> collectPathways() {
		final List<PathwayData> list = new LinkedList<>();
		analysisStoredResult.filterBySpecies(speciesDbId, resource).getPathways().stream()
				.limit(PdfProfile.MAX_PATHWAYS).forEach(pathwayBase -> {
			final PathwayNodeSummary pathwaySummary = analysisStoredResult.getPathway(pathwayBase.getStId());
			final Pathway pathway = GraphCoreHelper.getPathway(pathwayBase.getStId());
			list.add(new PathwayData(pathwaySummary, pathwayBase, pathway));
		});
		return list;
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
}
