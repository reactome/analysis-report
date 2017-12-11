package org.reactome.server.tools.analysis.exporter.playground.test;

import org.junit.Test;

import java.io.File;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class cleaner {
    @Test
    public void clean() throws Exception {
        for (File file : new File("src/main/resources/pdfs").listFiles()) file.delete();
    }
}