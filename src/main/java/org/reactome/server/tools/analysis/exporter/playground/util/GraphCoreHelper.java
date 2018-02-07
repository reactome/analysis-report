package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelper {

    private static ApplicationContext context;
    private static GeneralService genericService;
    private static DatabaseObjectService databaseObjectService;

    static {
        context = new AnnotationConfigApplicationContext(GraphCoreConfig.class);
        genericService = context.getBean(GeneralService.class);
        databaseObjectService = context.getBean(DatabaseObjectService.class);
    }

    /**
     * get pathways detail information from neo4j database by given the target pathway identifiers array.
     *
     * @param pathways {@see org.reactome.server.tools.analysis.exporter.playground.model.Pathway}
     * @return
     */
    public static Pathway[] getPathway(List<org.reactome.server.tools.analysis.exporter.playground.model.Pathway> pathways) {
        List<Object> identifiers = new ArrayList<>();
        pathways.forEach(pathway -> identifiers.add(pathway.getStId()));
        return databaseObjectService.findByIdsNoRelations(identifiers).toArray(new Pathway[identifiers.size()]);
    }

    /**
     * @return Reactome's current database version.
     */
    public static int getDBVersion() {
        return genericService.getDBVersion();
    }
}
