package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Assert;
import org.junit.Test;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

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
        Pathway pathway = advancedDatabaseObjectService.findEnhancedObjectById("R-HSA-5663202");
        LiteratureReference literatureReference = advancedDatabaseObjectService.findEnhancedObjectById("5663203");

        Assert.assertNotNull(pathway);
        assert 63 == genericService.getDBVersion();
        System.out.println(literatureReference.getJournal());
    }
}
