package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelper {

    private static final Logger logger = LoggerFactory.getLogger(GraphCoreHelper.class);
    private static GeneralService genericService;
    private static DatabaseObjectService databaseObjectService;

    static {
        // Instanciate our services
        ReactomeGraphCore.initialise("localhost", "7474", "neo4j", "reactome", GraphCoreConfig.class);

        genericService = ReactomeGraphCore.getService(GeneralService.class);
        databaseObjectService = ReactomeGraphCore.getService(DatabaseObjectService.class);
    }

    public static Pathway[] getPathway(org.reactome.server.tools.analysis.exporter.playground.domain.model.Pathway[] pathways) {
        long start = Instant.now().toEpochMilli();
        List<Object> identifiers = new ArrayList<>();
        Stream.of(pathways).forEach(pathway -> identifiers.add(pathway.getStId()));
        Pathway[] pathways1 = databaseObjectService.findByIdsNoRelations(identifiers).toArray(new Pathway[identifiers.size()]);
        logger.info("retrieve finish in :" + (Instant.now().toEpochMilli() - start) + " ms");
        return pathways1;
    }

    public static int getDBVersion() {
        return genericService.getDBVersion();
    }
}
