![reactome](https://reactome.org/templates/favourite/images/logo/logo.png)

Analysis Exporter
---
###Usage
* Pre-requirements  
    * Maven 3.+  
    * Java 8  
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
   
    String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
    File file = new File("dest/");
    PdfProperties properties = new PdfProperties(token);
    AnalysisExporter.export(properties, file);
```