package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatorChain {
    private List<Manipulator> manipulators = new ArrayList<Manipulator>();

    public ManipulatorChain addManipulator(Manipulator manipulator) {
        manipulators.add(manipulator);
        return this;
    }

    public void manipulate(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        for (Manipulator manipulator : manipulators) {
            manipulator.manipulatePDF(report,properties, dataSet);
        }
    }
}
