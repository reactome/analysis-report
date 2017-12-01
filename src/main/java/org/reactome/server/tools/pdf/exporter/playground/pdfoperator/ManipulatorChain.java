package org.reactome.server.tools.pdf.exporter.playground.pdfoperator;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatorChain {
    private List<PdfOperator> pdfOperators = new ArrayList<PdfOperator>();

    public ManipulatorChain addManipulator(PdfOperator pdfOperator) {
        pdfOperators.add(pdfOperator);
        return this;
    }

    public void manipulate(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        for (PdfOperator pdfOperator : pdfOperators) {
            pdfOperator.manipulatePDF(report,properties, dataSet);
        }
    }
}
