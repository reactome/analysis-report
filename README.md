![reactome](src/main/resources/org/reactome/server/tools/analysis/exporter/style/logo.png)

Analysis Exporter
---
AnalysisExporter is a tool to export the analysis result to a PDF document (PDF-1.7). Click here to know more about [Analysis Service](https://reactome.org/dev/analysis).
 
### Structure of the PDF

* __Cover page:__ summary of the analysis. Will help identify which analysis was performed and the date. 
* __Table of content__
* __Introduction:__ static text introducing Reactome Analysis.
* __Summary of Parameters and Results:__ detailedparameters used in analysis service and a genome-wide overview image:      
    ![fireworks](src/main/resources/readme/fireworks.png)

* __Top over-representation pathways sorted by p-Value:__   table contains the top pathways sorted by p-value with each statistics data  
    ![table_of_top_pathways](src/main/resources/readme/table_of_top_pathways.png)
    
* __Pathway details:__  detailed information for each pathway as listed afore and table of list identifiers found in that pathway  
    ![diagram](src/main/resources/readme/diagram.png)
    
* __Summary of identifiers found:__ all identifiers found in this analysis   
    ![identifiers_found](src/main/resources/readme/identifiers_found.png)
    
* __Summary of identifiers not found:__ all identifiers not found in this analysis　   
    ![identifiers_not_found](src/main/resources/readme/identifiers_not_found.png)
    

###Usage
* Pre-requirements  
    * Maven 3.+ 
    * Java 8 
    * [Neo4j](https://neo4j.com/)
* Install
```git
git clone https://bitbucket.org/fabregatantonio/analysis-report
cd analysis-report
mvn clean package
```

Since this module will retrieve Reactome Pathway data from the [Reactome Graph Database](https://reactome.org/dev/graph-database), You should install that in your local environment, if you don't have that yet or never experience on that, we strongly recommend you have a look on it.
To [configure reactome database](https://github.com/reactome/graph-core) and provide properties programmatically, we use `GraphCoreConfig` extends `Neo4jConfig` inside this module like:
 
```java
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"org.reactome.server.graph"})
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = {"org.reactome.server.graph.repository"})
@EnableSpringConfigured
public class GraphCoreConfig extends Neo4jConfig {
    private SessionFactory sessionFactory;
    private Logger logger = LoggerFactory.getLogger(GraphCoreConfig.class);

    @Bean
    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI("http://".concat(System.getProperty("neo4j.host")).concat(":").concat(System.getProperty("neo4j.port")))
                .setCredentials(System.getProperty("neo4j.user"), System.getProperty("neo4j.password"));
        return config;
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            logger.info("Creating a Neo4j SessionFactory");
            sessionFactory = new SessionFactory(getConfiguration(), "org.reactome.server.graph.domain");
        }
        return sessionFactory;
    }
}
```

So you need to initialise the graph-core before every thing like: 

```
    public static void initialise(String host, String port, String user, String password){
            System.setProperty("neo4j.host", host);
            System.setProperty("neo4j.port", port);
            System.setProperty("neo4j.user", user);
            System.setProperty("neo4j.password", password);
    }
```

Add AnalysisExporter as Maven dependency in your project: 

```
    <dependency>
        <groupId>org.reactome.server.tools</groupId>
        <artifactId>analysis-exporter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <!-- EBI repo -->
    <repository>
        <id>pst-release</id>
        <name>EBI Nexus Repository</name>
        <url>http://www.ebi.ac.uk/Tools/maven/repos/content/repositories/pst-release</url>
    </repository>
```

Use AnalysisExporter to export DPF document: 

``` java
// Configure Reactome graph database
ReactomeGraphCore.initialise("host", "port", "username", "password", GraphCoreConfig.class);

// Create an exporter with the paths to resources:
private static AnalysisExporter exporter = new AnalysisExporter(
	DIAGRAM_PATH,    // Layout(12345.json) and graph (12345.grap.json) files.
	EHLD_PATH,       // EHLD (R-HSA-123.svg) files
	FIREWORKS_PATH,  // Fireworks (Homo_sapiens.json) files
	ANALYSIS_PATH,   // Analysis (res_20210101_1.bin)
	SVG_SUMMARY);    // svgsummary.txt

OutputStream os = new FileOutputStream(new File("output.pdf"));

exporter.render(token, resource, 48887L, "breathe", 25, "modern", "copper plus", "barium lithium", os);
// or if you already host an analysis (AnalysisStoredResult)
exporter.render(analysis, resource, 48887L, "breathe", 25, "modern", "copper plus", "barium lithium", os);
```

###License
This module uses the [iText](https://itextpdf.com) library to create PDF document, so it naturally adopt the [![License](https://img.shields.io/badge/license-AGPL%203.0-blue.svg?style=plastic)](https://opensource.org/licenses/AGPL-3.0), 
you can use this module freely on condition that also comply with this license.
