package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface Manipulator {
    public void manipulatePDF(PdfReport report, PdfProperties properties, DataSet dataSet) throws Exception;
}
