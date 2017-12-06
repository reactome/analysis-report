package org.reactome.server.tools.analysis.exporter.playground.models._;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@SpringBootTest
public class _DataSetTests {
    @Test
    public void test() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        _DataSet dataSet = mapper.readValue(new File("src/main/java/org/reactome/server/tools/analysis/exporter/playground/models/_/_DataSet.json"),_DataSet.class);
        Assert.assertEquals(62,dataSet.getVersion());
        Assert.assertEquals(true,dataSet.getDetails()[0].isInDisease());
    }
}
