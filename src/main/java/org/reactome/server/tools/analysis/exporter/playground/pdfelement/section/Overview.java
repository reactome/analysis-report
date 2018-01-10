package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.IdentifiersWasFound;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.model.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableFactory;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.HttpClientHelper;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {
    private TableFactory tableFactory;

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        tableFactory = new TableFactory(report, dataSet);
        report.addNormalTitle("Overview")
                .addNormalTitle(String.format("1. Top %s Overrepresentation pathways sorted by p-Value.", report.getNumOfPathwaysToShow()), FontSize.H3, Indent.I3)
                .addTable(tableFactory.getTable(TableTypeEnum.OVERVIEW_TABLE))
                .addNormalTitle("2. Pathway details.", FontSize.H3, Indent.I3);

        addPathwaysDetails(report, dataSet);

        report.addNormalTitle("3. Identifiers was found.", FontSize.H3, Indent.I3, "IdentifiersWasFound")
                .addTable((dataSet.getIdentifiersWasFounds()[0].getExpNames().length != 0) ? tableFactory.getTable(TableTypeEnum.IdentifiersWasFound) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_FOUND_NO_EXP))
                .addNormalTitle("4. Identifiers was not found.", FontSize.H3, Indent.I3)
                .addTable((dataSet.getResultAssociatedWithToken().getExpression().getColumnNames().length != 0) ? tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND_NO_EXP));
    }

    // TODO: 14/12/17 this method should be reduce once the correct data structure confirm
    private void addPathwaysDetails(AnalysisReport report, DataSet dataSet) throws Exception {
        PathwayDetail pathwayDetail;
        Pathway[] pathways = dataSet.getPathways();
        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
        int length = report.getNumOfPathwaysToShow() <= dataSet.getPathways().length ? report.getNumOfPathwaysToShow() : dataSet.getPathways().length;
        for (int i = 0; i < length; i++) {
            pathwayDetail = HttpClientHelper.getForObject(URL.QUERYFORPATHWAYDETAIL, PathwayDetail.class, pathways[i].getStId());
            report.addNormalTitle(String.format("2.%s. %s ({})", i + 1, pathways[i].getName()), FontSize.H3, Indent.I4, pathways[i].getName(), new Link(pathways[i].getStId(), PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())));

            // TODO: 29/11/17 add the correct diagram;
            report.addDiagram("R-HSA-169911", dataSet.getReportArgs())
                    .addNormalTitle("Summation", FontSize.H4, Indent.I4)
                    .addParagraph("species name:" +
                                    pathwayDetail.getSpeciesName() +
                                    (pathwayDetail.getCompartment() != null ? ",compartment name:" + pathwayDetail.getCompartment()[0].getDisplayName() : "") +
                                    (pathwayDetail.isInDisease() ? ",disease name:" + pathwayDetail.getDisease()[0].getDisplayName() : "") +
                                    (pathwayDetail.isInferred() ? ",inferred from:" + pathwayDetail.getInferredFrom()[0].getDisplayName() : "") +
                                    (pathwayDetail.getSummation() != null ? "," + pathwayDetail.getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                            , FontSize.H5, 0, MarginLeft.M5);

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, Indent.I4)
                    .addTable(tableFactory.getTable(identifiersWasFounds[i].getEntities()));
            if (pathwayDetail.getAuthors() != null) {
                addCuratorDetail(report, "Authors", pathwayDetail.getAuthors().getDisplayName());
            }
            if (pathwayDetail.getEditors() != null) {
                addCuratorDetail(report, "Editors", pathwayDetail.getEditors().getDisplayName());
            }
            if (pathwayDetail.getReviewers() != null) {
                addCuratorDetail(report, "Reviewers", pathwayDetail.getReviewers()[0].getDisplayName());
            }
            if (pathwayDetail.getLiteratureReference() != null) {
                addCuratorDetail(report, "References", pathwayDetail.getLiteratureReference()[0].toString());
            }
        }

//    private void addPathwaysDetails(AnalysisReport report, DataSet dataSet) throws Exception {
//        PathwayDetail[] pathwayDetail;
//        Pathway[] pathways = dataSet.getPathways();
//        StringBuilder stringBuilder = PdfUtils.stIdConcat(Arrays.copyOf(pathways, report.getNumOfPathwaysToShow()));
//        pathwayDetail = HttpClientHelper.postForObject("https://reactome.org/ContentService/data/query/ids", stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(), PathwayDetail[].class, "");
//        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
//        int length = report.getNumOfPathwaysToShow() <= dataSet.getPathways().length ? report.getNumOfPathwaysToShow() : dataSet.getPathways().length;
//        for (int i = 0; i < length; i++) {
//            report.addNormalTitle(String.format("2.%s. %s ({})", i + 1, pathways[i].getName()), FontSize.H3, Indent.I4, pathways[i].getName(), new Link(pathways[i].getStId(), PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())));
//
//            // TODO: 29/11/17 add the correct diagram;
//            report.addDiagram("R-HSA-169911", dataSet.getReportArgs())
//                    .addNormalTitle("Summation", FontSize.H4, Indent.I4)
//                    .addParagraph("species name:" +
//                                    pathwayDetail[i].getSpeciesName() +
//                                    (pathwayDetail[i].getCompartment() != null ? ",compartment name:" + pathwayDetail[i].getCompartment()[0].getDisplayName() : "") +
//                                    (pathwayDetail[i].isInDisease() ? ",disease name:" + pathwayDetail[i].getDisease()[0].getDisplayName() : "") +
//                                    (pathwayDetail[i].isInferred() ? ",inferred from:" + pathwayDetail[i].getInferredFrom()[0].getDisplayName() : "") +
//                                    (pathwayDetail[i].getSummation() != null ? "," + pathwayDetail[i].getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
//                            , FontSize.H5, 0, MarginLeft.M5);
//
//            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, Indent.I4)
//                    .addTable(tableFactory.getTable(identifiersWasFounds[i].getEntities()));
//            if (pathwayDetail[i].getAuthors() != null) {
//                addCuratorDetail(report, "Authors", pathwayDetail[i].getAuthors().getDisplayName());
//            }
//            if (pathwayDetail[i].getEditors() != null) {
//                addCuratorDetail(report, "Editors", pathwayDetail[i].getEditors().getDisplayName());
//            }
//            if (pathwayDetail[i].getReviewers() != null) {
//                addCuratorDetail(report, "Reviewers", pathwayDetail[i].getReviewers()[0].getDisplayName());
//            }
//            if (pathwayDetail[i].getLiteratureReference() != null) {
//                addCuratorDetail(report, "References", pathwayDetail[i].getLiteratureReference()[0].toString());
//            }
//        }
    }

    private void addCuratorDetail(AnalysisReport report, String title, String content) {
        report.addNormalTitle(title, FontSize.H4, Indent.I4)
                .addParagraph(content, FontSize.H5, Indent.I5, MarginLeft.M0);
    }
}
