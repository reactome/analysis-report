package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.P;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Section Introduction contains the analysis introduction and Reactome relative publications
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

    private static final List<String> INTRODUCTION = PdfUtils.getText("texts/introduction.txt");
    private static final List<String> PUBLICATIONS = PdfUtils.getText("texts/references.txt");

    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) {
        report.add(new Header("1: Introduction", FontSize.H1).setDestination("introduction"));
        for (String introduction : INTRODUCTION) {
            report.add(new P(introduction));
        }

        List<Paragraph> list = new ArrayList<>();
        for (String publication : PUBLICATIONS) {

            // Use '<>' symbol to split the publication text into literal one and the link url,
            String[] text = publication.split("<>");
            list.add(new P(text[0])
                    .add(PdfUtils.getLinkIcon(text[1])));
        }
        report.addAsList(list);
    }
}