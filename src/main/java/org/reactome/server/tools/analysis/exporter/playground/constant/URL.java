package org.reactome.server.tools.analysis.exporter.playground.constant;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
public class URL {
    public static final String REACTOME = "https://reactome.org";
    public static final String IDENTIFIERSWASNOTFOUND = "https://reactome.org/AnalysisService/token/%s/notFound";
    public static final String VERSION = "https://reactome.org/ContentService/data/database/version";
    public static final String ANALYSIS = "https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=";
    public static final String IDENTIFIERSWASFOUND = "https://reactome.org/AnalysisService/token/%s/found/all";
    public static final String QUERYFORPATHWAYDETAILS = "https://reactome.org/content/query?q=";
    public static final String QUERYFORPATHWAYDETAIL = "https://reactome.org/ContentService/data/query/enhanced/%s";
    //    public static final String RESULTASSCIATEDWITHTOKEN = "https://reactome.org/AnalysisService/token/{token}?pageSize={numberOfPathwaysToShow}&page=1&sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";
    public static final String RESULTASSCIATEDWITHTOKEN = "https://reactome.org/AnalysisService/token/%s?sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";
}
