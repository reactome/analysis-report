package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfProfileTest {
    @Test
    public void test() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        mapper.writeValue(new File("src/test/resources/properties.json"),new PdfProfileTest());
        System.out.println(mapper.writeValueAsString(new PdfProfile()));
        mapper.writeValue(new File("src/test/resources/properties.json"), new PdfProfile());
    }
}
