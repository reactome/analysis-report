package org.reactome.server.tools.analysis.report;

import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.Collection;
import java.util.Collections;
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
	private final AnalysisStoredResult result;
	private final Long speciesDbId;
	private final int maxPathways;
	private final boolean projection;
	private final boolean interactors;
	private final Collection<String> resources;

	AnalysisData(AnalysisStoredResult result, String resource, Long speciesDbId, int maxPathways) {
		this.result = result;
		this.resource = resource;
		this.speciesDbId = speciesDbId;
		this.beautifiedResource = beautify(resource);
		this.species = getSpeciesName(speciesDbId);
		this.type = AnalysisType.valueOf(result.getSummary().getType());
		this.speciesComparisonSpecies = getSpeciesName(result.getSummary().getSpecies());
		this.maxPathways = maxPathways;
		this.name = computeName();
		this.projection = result.getSummary().isProjection() != null && result.getSummary().isProjection();
		this.interactors = result.getSummary().isInteractors() != null && result.getSummary().isInteractors();
		this.resources = (resource.equals("TOTAL"))
				? result.getResourceSummary().stream()
				.map(ResourceSummary::getResource)
				.filter(s -> !s.equals("TOTAL"))
				.map(this::beautify)
				.collect(Collectors.toList())
				: Collections.singletonList(beautify(resource));

		pathways = collectPathways();
	}

	private static String getSpeciesName(Long id) {
		if (id == null) return null;
		Species species = databaseObjectService.findByIdNoRelations(id);
		return species.getDisplayName();
	}

	private List<PathwayData> collectPathways() {
		return result.filterBySpecies(speciesDbId, resource).getPathways().stream()
				.limit(maxPathways)
				.map(base -> {
					final PathwayNodeSummary summary = result.getPathway(base.getStId());
					final Pathway pathway = databaseObjectService.findByIdNoRelations(base.getStId());
					return (new PathwayData(summary, base, pathway));
				})
				.collect(Collectors.toList());
	}

	private String computeName() {
		for (String alternative : new String[]{
				result.getSummary().getSampleName(),
				result.getSummary().getFileName(),
				speciesComparisonSpecies,
				result.getSummary().getType(),
				result.getSummary().getToken()}) {
			if (alternative != null) return alternative;
		}
		return "";
	}

	private String beautify(String resource) {
		switch (resource.toLowerCase()) {
			case "uniprot":
				return "UniProt";
			case "chebi":
				return "ChEBI";
			case "ensembl":
				return "Ensembl";
			case "kegg":
				return "KEGG";
			case "pubmed":
				return "PubMed";
			case "total":
				return "all resources";
			default:
				return resource;
		}
	}

	/**
	 * @return Reactome's current database version.
	 */
	public static int getDBVersion() {
		return genericService.getDBVersion();
	}

	public AnalysisStoredResult getResult() {
		return result;
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

	/**
	 * Get the list of all resources present in this analysis. If one resource
	 * is specified in the analysis, then this is a singleton list. If resource
	 * is TOTAL, the gets a list with all the resources but TOTAL.
	 * <p>
	 * This is a convenient method to list tables of identifiers.
	 *
	 * @return a list with all of the resources present in the analysis.
	 */
	public Collection<String> getResources() {
		return resources;
	}
}
