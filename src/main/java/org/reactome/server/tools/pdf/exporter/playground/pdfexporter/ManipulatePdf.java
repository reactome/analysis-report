package org.reactome.server.tools.pdf.exporter.playground.pdfexporter;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.manipulator.*;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatePdf {
    private static ManipulatorChain manipulatorChain;
    private static DataSet dataSet;
    private static PdfReport report;

    public static void manipulate(PdfProperties properties) throws Exception {
        dataSet = PdfUtils.getDataSet(properties);
        report = new PdfReport(properties);
        manipulatorChain = new ManipulatorChain();
        manipulatorChain.addManipulator(new FooterEvent())
                .addManipulator(new TitleAndLogo())
                .addManipulator(new Administrative())
                .addManipulator(new Introduction())
                .addManipulator(new Summary())
                .addManipulator(new Details());
        manipulatorChain.manipulate(report, properties, dataSet);
        report.close();
        properties.close();
    }
}
