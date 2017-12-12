![reactome](https://reactome.org/templates/favourite/images/logo/logo.png)
#Analysis Report
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
    String token = "MjAxNzExMTcwODEzMjBfNzU%253D";
    File file = new File("dest/");
    PdfProperties properties = new PdfProperties(token);
    AnalysisExporter.export(properties, file);
```