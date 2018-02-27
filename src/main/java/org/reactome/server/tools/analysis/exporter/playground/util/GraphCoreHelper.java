package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.graph.domain.model.DatabaseObject;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.service.GeneralService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Help to retrieve information from Reactome GraphCore.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelper {

    private static ApplicationContext context;
    private static GeneralService genericService;
    private static DiagramService diagramService;

    private static DatabaseObjectService databaseObjectService;

    static {
        context = new AnnotationConfigApplicationContext(GraphCoreConfig.class);
        genericService = context.getBean(GeneralService.class);
        diagramService = context.getBean(DiagramService.class);
        databaseObjectService = context.getBean(DatabaseObjectService.class);
    }

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
        Species species = databaseObjectService.findByIdNoRelations(id);
        return species.getName().get(0);
    }

    public static String getFoundEntityName(String stId) {
        DatabaseObject result = databaseObjectService.findById(stId);
        if (result != null) {
            return result.getDisplayName();
        } else {
            return stId;
        }
    }

    public static DiagramResult getDiagramResult(String stId) {
        return diagramService.getDiagramResult(stId);
    }
}