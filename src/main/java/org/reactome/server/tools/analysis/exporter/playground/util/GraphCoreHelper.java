package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.graph.domain.model.DatabaseObject;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelper {

    private static GeneralService genericService;
    private static DatabaseObjectService databaseObjectService;

    static {
        final ApplicationContext context = new AnnotationConfigApplicationContext(GraphCoreConfig.class);
        genericService = context.getBean(GeneralService.class);
        databaseObjectService = context.getBean(DatabaseObjectService.class);
    }

    /**
     * @return Reactome's current database version.
     */
    public static int getDBVersion() {
        return genericService.getDBVersion();
    }

    /**
     * get pathways detail information from neo4j database by given the target pathway identifiers array.
     *
     * @return {@code Pathway} contains the pathway detail information.
     */
    public static Pathway getPathway(String stId) {
        return databaseObjectService.findByIdNoRelations(stId);
    }

    public static String getSpeciesName(Long id) {
        if (null == id) {
            return null;
        } else {
            final Species species = databaseObjectService.findByIdNoRelations(id);
            return species.getName().get(0);
        }
    }

    public static String getFoundEntityName(String stId) {
        DatabaseObject result = databaseObjectService.findById(stId);
        if (result != null) {
            return result.getDisplayName();
        } else {
            return stId;
        }
    }
}