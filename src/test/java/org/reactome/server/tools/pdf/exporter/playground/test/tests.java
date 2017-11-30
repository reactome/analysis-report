package org.reactome.server.tools.pdf.exporter.playground.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@SpringBootTest
public class tests {
    @Test
    public void test(){
        File file = new File("src/main/recources/AnalysisReport/");
        System.out.println(file.getName());
    }
}
