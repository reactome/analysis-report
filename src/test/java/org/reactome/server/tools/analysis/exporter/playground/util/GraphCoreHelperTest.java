package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelperTest {
    @Test
    public void test() {
        // Initialising ReactomeCore Neo4j configuration
        ReactomeGraphCore.initialise("localhost", "7474", "neo4j", "reactome", GraphCoreConfig.class);
        // Instanciate our services
        GeneralService genericService = ReactomeGraphCore.getService(GeneralService.class);
        DatabaseObjectService databaseObjectService = ReactomeGraphCore.getService(DatabaseObjectService.class);
        AdvancedDatabaseObjectService advancedDatabaseObjectService = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);
        long start = Instant.now().toEpochMilli();
//        for (int i = 0; i < 50; i++) {
//            Pathway pathway = databaseObjectService.findById("R-HSA-5663202");
//            System.out.println(pathway.getEdited() == null ? "null" : pathway.getEdited().toString());
//            System.out.println(pathway.getAuthored() == null ? "null" : pathway.getAuthored().toString());
//            System.out.println(pathway.getCreated() == null ? "null" : pathway.getCreated().toString());
//        }
        Pathway pathway = advancedDatabaseObjectService.findEnhancedObjectById("R-HSA-5663202");
        LiteratureReference databaseObject = advancedDatabaseObjectService.findEnhancedObjectById("5663203");
        System.out.println(pathway.getDisplayName());
//        List<Object> identifiers = new ArrayList<>();
//        identifiers.add("R-HSA-5663202");
//        System.out.println(identifiers.toString());
//        List<Pathway> pathways = Arrays.asList(databaseObjectService.findByIdsNoRelations(identifiers).toArray(new Pathway[identifiers.size()]));
//        System.out.println(Instant.now().toEpochMilli() - start);
//        System.out.println(pathways.toString());

    }
}
