![reactome](src/main/resources/org/reactome/server/tools/analysis/exporter/playground/util/images/logo.png)

Analysis Exporter
---
AnalysisExporter is a tool to export the analysis result (performed by Reactome [Analysis tools](https://reactome.org/PathwayBrowser/#TOOL=AT)) to a PDF document (PDF-1.7). 
 
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

Since this module will retrieve Reactome Pathway data from the [Reactome Graph Database](https://reactome.org/dev/graph-database) , you should install that in your local environment, if you don't have that yet or never experience on that, we strongly recommend that you have a look on it.
To follow the good practice, the Neo4j's Java system property name should be: "neo4j.host", "noe4j.port", "neo4j.user" and "neo4j.password", those will be access by `System.getProperty("neo4j.xxxx")`.

Add Analysis Exporter as maven dependency in your project

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
Use AnalysisExporter to export DPF document
```
    // This path must contain the layout and graph json files.
    // You can download them from https://reactome.org/download/current/diagram/
    String DIAGRAM_PATH = "diagram/path";
    
    // This path must contain the EHLD svg file
    // You can download them from https://reactome.org/download/current/ehld/
    String EHLD_PATH = "ehld/path";
    
    // This path contasins ths svgSummary file.
    // You will also find a file containing a list of available EHLD: https://reactome.org/download/current/ehld/svgsummary.txt
    String svgSummary = "directory/svgSummary.txt";

    // This path contains the fireworks layout json files. 
    // You can download the file from https://reactome.org/download/current/fireworks/
    String FIREWORKS_PATH = "fireworks/path";
    
    // This path contains the Reactome analysis binary files.
    String ANALYSIS_PATH = "analysis/path";
    
    ReportArgs reportArgs = new ReportArgs("MjAxODAyMTIxMTI5MzdfMQ==", DIAGRAM_PATH, EHLD_PATH, FIREWORKS_PATH, ANALYSIS_PATH, svgSummary);
    
    // Save the PDF document to the local directory.
    FileOutputStream fos = new FileOutputStream(new File("directory/fileName.pdf"));
    AnalysisExporter.export(reportArgs, fos);
    
    // Or hold the PDF Document as a OutputStream so you can pass it by any http method.
    OutputStream os = new ByteArrayOutputStream();
    AnalysisExporter.export(reportArgs, os);
    ...
```


###License
This module use the [iText](https://itextpdf.com) library to create PDF document, so it naturally adopted the [![License](https://img.shields.io/badge/license-AGPL%203.0-blue.svg?style=plastic)](https://opensource.org/licenses/AGPL-3.0), 
you can use this module freely on condition that also comply with this license.