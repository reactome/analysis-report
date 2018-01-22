package org.reactome.server.tools.analysis.exporter.playground.constant;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class URL {
    public static final String CHECKTOKEN = "https://reactome.org/AnalysisService/token/%s/resources";
    public static final String REACTOME = "https://reactome.org";
    public static final String IDENTIFIERSWASNOTFOUND = "https://reactome.org/AnalysisService/token/%s/notFound";
    public static final String VERSION = "https://reactome.org/ContentService/data/database/version";
    public static final String ANALYSIS = "https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=";
    public static final String IDENTIFIERSWASFOUND = "https://reactome.org/AnalysisService/token/%s/found/all";
    public static final String QUERYFORPATHWAYDETAILS = "https://reactome.org/content/detail/";
    public static final String QUERYFORPATHWAYDETAIL = "https://reactome.org/ContentService/data/query/enhanced/%s";
    //    public static final String RESULTASSCIATEDWITHTOKEN = "https://reactome.org/AnalysisService/token/{token}?pageSize={numberOfPathwaysToShow}&page=1&sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";
    public static final String RESULTASSCIATEDWITHTOKEN = "https://reactome.org/AnalysisService/token/%s?sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL";

    /**
     * https://academic.oup.com/nar/article/44/D1/D481/2503122
     * https://academic.oup.com/nar/article/45/D1/D985/2605745
     * https://academic.oup.com/bioinformatics/article/33/21/3461/3930125
     *
     * https://www.ncbi.nlm.nih.gov/pubmed/12509752
     * https://www.ncbi.nlm.nih.gov/pubmed/15520807
     * https://www.ncbi.nlm.nih.gov/pubmed/21252999
     */
}
