package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.ListParagraph;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

    private static final String[] INTRODUCTION = PdfUtils.getText("../texts/introduction.txt");
    private static final String[] LITERATURE = PdfUtils.getText("../texts/references.txt");

    public void render(AnalysisReport report, AnalysisStoredResult result, SpeciesFilteredResult speciesFilteredResult) {
        report.add(new Header("1: Introduction", FontSize.H2));
        for (String introduction : INTRODUCTION) {
            report.add(new P(introduction));
        }

        List<Paragraph> list = new ArrayList<>();
        for (int i = 0; i < LITERATURE.length; i++) {
            list.add(new ListParagraph(LITERATURE[i])
                    .setFontSize(FontSize.H5)
                    .add(PdfUtils.getLinkIcon(LITERATURE[++i])));
        }
        report.addAsList(list);
    }
}