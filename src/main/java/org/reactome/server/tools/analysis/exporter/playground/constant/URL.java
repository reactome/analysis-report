package org.reactome.server.tools.analysis.exporter.playground.constant;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class URL {
    /**
     * url from analysis service
     */
    public static final String CHECKTOKEN = "https://reactome.org/AnalysisService/token/%s/resources";
    //get identifiers was found
    public static final String IDENTIFIERSWASFOUND = "https://reactome.org/AnalysisService/token/%s/found/all";
    //get identifiers was not found
    public static final String IDENTIFIERSWASNOTFOUND = "https://reactome.org/AnalysisService/token/%s/notFound";
    //get whole overview information
    public static final String RESULTASSCIATEDWITHTOKEN = "https://reactome.org/AnalysisService/token/%s?sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";

    /**
     * url just for literal link jump
     */
    public static final String REACTOME = "https://reactome.org";
    public static final String QUERYFORPATHWAYDETAILS = "https://reactome.org/content/detail/";
    public static final String ANALYSIS = "https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=";
}
