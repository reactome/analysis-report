package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class GraphCoreHelperTest {

    @Test
    public void test() {
        Assert.assertEquals("Homo sapiens", GraphCoreHelper.getSpeciesName(48887L));
    }
}