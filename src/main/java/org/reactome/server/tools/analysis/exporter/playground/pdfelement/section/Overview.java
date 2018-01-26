package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableFactory;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    public void render(AnalysisReport report, DataSet dataSet) throws Exception {
        TableFactory tableFactory = new TableFactory(dataSet);
        report.addNormalTitle("Overview")
                .addNormalTitle(String.format("1. Top %s Overrepresentation pathways sorted by p-Value.", dataSet.getPathwaysToShow()), FontSize.H3, Indent.I3)
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

        Pathway[] pathways = GraphCoreHelper.getPathway(dataSet.getResultAssociatedWithToken().getPathways());
        PathwayDetail pathwayDetail;
        LiteratureReference literatureReference = null;
        for (int i = 0; i < dataSet.getPathwaysToShow(); i++) {
            pathwayDetail = new PathwayDetail(pathways[i]);
            report.addNormalTitle(new Paragraph(String.format("2.%s. %s (%s", i + 1, dataSet.getResultAssociatedWithToken().getPathways()[i].getName(), dataSet.getResultAssociatedWithToken().getPathways()[i].getStId()))
                            .add(PdfUtils.createUrlLinkIcon(FontSize.H3, URL.QUERYFORPATHWAYDETAILS + dataSet.getResultAssociatedWithToken().getPathways()[i].getStId()))
                            .add(")")
                    , FontSize.H3, Indent.I4, dataSet.getResultAssociatedWithToken().getPathways()[i].getName());

            // TODO: 29/11/17 add the correct diagram;
            report.addDiagram("R-HSA-169911", dataSet.getReportArgs());

            report.addNormalTitle("Summation", FontSize.H4, Indent.I4)
                    .addParagraph("species name:" +
                                    pathways[i].getSpeciesName() +
                                    (pathwayDetail.getCompartments() != null ? ",compartment name:" + pathwayDetail.getCompartments().get(0).getDisplayName() : "") +
                                    (pathways[i].getIsInDisease() ? ",disease name:" + pathwayDetail.getDiseases().get(0).getDisplayName() : "") +
                                    (pathways[i].getIsInferred() ? ",inferred from:" + pathwayDetail.getEvents().iterator().next().getDisplayName() : "") +
                                    (pathwayDetail.getSummations() != null ? "," + pathwayDetail.getSummations().get(0).getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                            , FontSize.H5, 0, MarginLeft.M5);

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, Indent.I4)
                    .addTable(tableFactory.getTable(dataSet.getIdentifiersWasFounds()[i].getEntities()));
            if (pathwayDetail.getAuthored() != null) {// TODO: 25/01/18 add all curator info
                addCuratorDetail(report, "Authors", PdfUtils.getInstanceEditNames(pathwayDetail.getAuthored()));
            }
            if (pathwayDetail.getEdited() != null) {
                addCuratorDetail(report, "Editors", PdfUtils.getInstanceEditNames(pathwayDetail.getEdited()));
            }
            if (pathwayDetail.getModified() != null) {
                addCuratorDetail(report, "Reviewers", PdfUtils.getInstanceEditName(pathwayDetail.getModified()));
            }
            if (pathwayDetail.getPublications() != null) {
                report.addNormalTitle("References", FontSize.H4, Indent.I4);
                int length = pathwayDetail.getPublications().size() > 5 ? 5 : pathwayDetail.getPublications().size();
                for (int j = 0; j < length; j++) {
                    if ("LiteratureReference".equals(pathwayDetail.getPublications().get(j).getSchemaClass())) {
                        literatureReference = (LiteratureReference) pathwayDetail.getPublications().get(j);
                        report.addParagraph(new Paragraph(String.format("%s \"%s\", %s, %s, %s, %s."
                                , PdfUtils.getAuthorDisplayName(literatureReference.getAuthor())
                                , literatureReference.getTitle()
                                , literatureReference.getJournal()
                                , literatureReference.getVolume()
                                , literatureReference.getYear()
                                , literatureReference.getPages()))
                                .add(PdfUtils.createUrlLinkIcon(FontSize.H5, literatureReference.getUrl()))
                                .setFontSize(FontSize.H5)
                                .setFirstLineIndent(Indent.I5)
                                .setMarginLeft(MarginLeft.M0)
                        );
                    }
                }
            }
        }
    }

    private void addCuratorDetail(AnalysisReport report, String title, String content) {
        report.addNormalTitle(title, FontSize.H4, Indent.I4)
                .addParagraph(content, FontSize.H5, Indent.I5, MarginLeft.M0);
    }
}
