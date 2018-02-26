![reactome](src/main/resources/org/reactome/server/tools/analysis/exporter/playground/util/images/logo.png)

Analysis Exporter
---
AnalysisExporter is a tool to export the analysis result performed by Reactome [Analysis tools](https://reactome.org/PathwayBrowser/#TOOL=AT) to a PDF document so you can review it after the online analysis. 
This PDF contains the fireworks overview image and diagram image for each pathway hit in analysis. 
 
###Usage
* Pre-requirements  
    * Maven 3.+  
    * Java 8  
    * [Reactome Graph Database](https://reactome.org/dev/graph-database)
* Install
```git
git clone https://github.com/xxx
```

```
    -o <output> output folder to save created pdf file
    -t <token> analysis token from reactome analysis service with your data set
    -d <diagramPath> static path contains the diagram raw information json file
    -e <ehdlPath> static path contains the ehld raw information json file
    -f <fireworksPath> static path contains the fireworks raw information json file
   
     String token = "MjAxODAxMDEwNzUwMjdfMTc%253D";
     String diagramPath = "/home/byron/static/demo";
     String ehldPath = "/home/byron/static";
     String fireworksPath = "/home/byron/json";
                
     FileOutputStream outputStream = new FileOutputStream(new File("dest.pdf"));
     ReportArgs reportArgs = new ReportArgs(token, diagramPath, ehldPath, fireworksPath);
     AnalysisExporter.export(reportArgs, outputStream);
     ...
     outputStream.close();
```
###License
This module used the [iText](https://itextpdf.com) library to create PDF documents,so it followed the `AGPL` license.  
[![License](https://img.shields.io/badge/license-AGPL%203.0-blue.svg?style=plastic)](https://opensource.org/licenses/AGPL-3.0)  
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=plastic)](https://opensource.org/licenses/Apache-2.0)