package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableFactory;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.HttpClientHelper;

import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        TableFactory tableFactory = new TableFactory(dataSet);
        report.addNormalTitle("Overview")
                .addNormalTitle(String.format("1. Top %s Overrepresentation pathways sorted by p-Value.", dataSet.getNumOfPathwaysToShow()), FontSize.H3, Indent.I3)
                .addTable(tableFactory.getTable(TableTypeEnum.OVERVIEW_TABLE))
                .addNormalTitle("2. Pathway details.", FontSize.H3, Indent.I3);

        addPathwaysDetails(report, dataSet, tableFactory);

        report.addNormalTitle("3. Identifiers was found.", FontSize.H3, Indent.I3, "IdentifiersWasFound")
                .addTable((dataSet.getIdentifiersWasFounds()[0].getExpNames().length != 0) ? tableFactory.getTable(TableTypeEnum.IdentifiersWasFound) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_FOUND_NO_EXP))
                .addNormalTitle("4. Identifiers was not found.", FontSize.H3, Indent.I3)
                .addTable((dataSet.getResultAssociatedWithToken().getExpression().getColumnNames().length != 0) ? tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND_NO_EXP));
    }

    // TODO: 14/12/17 this method should be reduce once the correct data structure confirm
    private void addPathwaysDetails(AnalysisReport report, DataSet dataSet, TableFactory tableFactory) throws Exception {
        List<PathwayDetail> pathwayDetails = HttpClientHelper.getPathwayDetails(dataSet.getResultAssociatedWithToken().getPathways());
        for (int i = 0; i < dataSet.getNumOfPathwaysToShow(); i++) {
            report.addNormalTitle(String.format("2.%s. %s ({})", i + 1, dataSet.getResultAssociatedWithToken().getPathways()[i].getName()), FontSize.H3, Indent.I4, dataSet.getResultAssociatedWithToken().getPathways()[i].getName()
                    , new Link(dataSet.getResultAssociatedWithToken().getPathways()[i].getStId()
                            , PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS + dataSet.getResultAssociatedWithToken().getPathways()[i].getStId())));
            // TODO: 29/11/17 add the correct diagram;
            report.addDiagram("R-HSA-169911", dataSet.getReportArgs())
                    .addNormalTitle("Summation", FontSize.H4, Indent.I4)
                    .addParagraph("species name:" +
                                    pathwayDetails.get(i).getSpeciesName() +
                                    (pathwayDetails.get(i).getCompartment() != null ? ",compartment name:" + pathwayDetails.get(i).getCompartment()[0].getDisplayName() : "") +
                                    (pathwayDetails.get(i).isInDisease() ? ",disease name:" + pathwayDetails.get(i).getDisease()[0].getDisplayName() : "") +
                                    (pathwayDetails.get(i).isInferred() ? ",inferred from:" + pathwayDetails.get(i).getInferredFrom()[0].getDisplayName() : "") +
                                    (pathwayDetails.get(i).getSummation() != null ? "," + pathwayDetails.get(i).getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                            , FontSize.H5, 0, MarginLeft.M5);

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, Indent.I4)
                    .addTable(tableFactory.getTable(dataSet.getIdentifiersWasFounds()[i].getEntities()));
            if (pathwayDetails.get(i).getAuthors() != null) {
                addCuratorDetail(report, "Authors", pathwayDetails.get(i).getAuthors().getDisplayName());
            }
            if (pathwayDetails.get(i).getEditors() != null) {
                addCuratorDetail(report, "Editors", pathwayDetails.get(i).getEditors().getDisplayName());
            }
            if (pathwayDetails.get(i).getReviewers() != null) {
                addCuratorDetail(report, "Reviewers", pathwayDetails.get(i).getReviewers()[0].getDisplayName());
            }
            if (pathwayDetails.get(i).getLiteratureReference() != null) {
                addCuratorDetail(report, "References", pathwayDetails.get(i).getLiteratureReference()[0].toString());
            }
        }
    }

    private void addCuratorDetail(AnalysisReport report, String title, String content) {
        report.addNormalTitle(title, FontSize.H4, Indent.I4)
                .addParagraph(content, FontSize.H5, Indent.I5, MarginLeft.M0);
    }
}
