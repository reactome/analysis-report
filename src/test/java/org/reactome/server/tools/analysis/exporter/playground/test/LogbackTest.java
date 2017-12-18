package org.reactome.server.tools.analysis.exporter.playground.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class LogbackTest {
    @Test
    public void test(){
        Logger logger = LoggerFactory.getLogger("logback test");
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}
