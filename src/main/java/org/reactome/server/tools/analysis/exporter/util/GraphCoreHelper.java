package org.reactome.server.tools.analysis.exporter.util;

import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

/**
 * Help to retrieve information from Reactome GraphCore.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelper {

	private static GeneralService genericService = ReactomeGraphCore.getService(GeneralService.class);
	private static DiagramService diagramService = ReactomeGraphCore.getService(DiagramService.class);
	private static DatabaseObjectService databaseObjectService = ReactomeGraphCore.getService(DatabaseObjectService.class);

	/**
	 * @return Reactome's current database version.
	 */
	public static int getDBVersion() {
		return genericService.getDBVersion();
	}

	/**
	 * retrieve pathway's detail information from the graph core.
	 *
	 * @param stId pathway's stable identifier.
	 */
	public static Pathway getPathway(String stId) {
		return databaseObjectService.findByIdNoRelations(stId);
	}

	public static String getSpeciesName(Long id) {
		if (id == null) return null;
		Species species = databaseObjectService.findByIdNoRelations(id);
		return species.getName().get(0);
	}

	public static DiagramResult getDiagramResult(String stId) {
		return diagramService.getDiagramResult(stId);
	}
}
