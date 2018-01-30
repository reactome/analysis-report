package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.model.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableFactory;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    public void render(AnalysisReport report) throws Exception {
        TableFactory tableFactory = new TableFactory(report.getDataSet());

        addOverviewTable(report, tableFactory);

        addPathwaysDetail(report, tableFactory);

        addIdentifierTable(report, tableFactory);
    }

    private void addOverviewTable(AnalysisReport report, TableFactory tableFactory) throws FailToAddLogoException, NullLinkIconDestinationException, TableTypeNotFoundException {
        report.addNormalTitle("Overview")
                .addNormalTitle(String.format("1. Top %s Overrepresentation pathways sorted by p-Value.", report.getDataSet().getPathwaysToShow()), FontSize.H3, Indent.I3)
                .addTable(tableFactory.getTable(TableTypeEnum.OVERVIEW_TABLE))
                .addNormalTitle("2. Pathway details.", FontSize.H3, Indent.I3);

    }

    private void addIdentifierTable(AnalysisReport report, TableFactory tableFactory) throws FailToAddLogoException, NullLinkIconDestinationException, TableTypeNotFoundException {
        report.addNormalTitle("3. Identifiers was found.", FontSize.H3, Indent.I3, "IdentifierFound")
                .addTable((report.getDataSet().getIdentifierFounds().get(0).getExpNames().size() != 0) ? tableFactory.getTable(TableTypeEnum.IdentifiersWasFound) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_FOUND_NO_EXP))
                .addNormalTitle("4. Identifiers was not found.", FontSize.H3, Indent.I3)
                .addTable((report.getDataSet().getAnalysisResult().getExpression().getColumnNames().size() != 0) ? tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND) : tableFactory.getTable(TableTypeEnum.IDENTIFIERS_WAS_NOT_FOUND_NO_EXP));
    }

    // TODO: 14/12/17 this method need to be optimize
    private void addPathwaysDetail(AnalysisReport report, TableFactory tableFactory) throws Exception {

//        List<Pathway> pathways = GraphCoreHelper.getPathway(report.getDataSet().getAnalysisResult().getPathways());
        Pathway[] pathways = GraphCoreHelper.getPathway(report.getDataSet().getAnalysisResult().getPathways());
        PathwayDetail pathwayDetail;
        for (int i = 0; i < report.getDataSet().getPathwaysToShow(); i++) {
            pathwayDetail = new PathwayDetail(pathways[i]);

            addTitleAndDiagram(report, report.getDataSet().getAnalysisResult().getPathways().get(0), i);
            addSummationAndTable(report, pathways[i], pathwayDetail, tableFactory.getTable(report.getDataSet().getIdentifierFounds().get(i).getEntities()));
            addCuratorDetail(report, pathwayDetail);
            addLiteratureReference(report, pathwayDetail);
            pathways[i] = null;
            report.getDataSet().getAnalysisResult().getPathways().remove(0);
            report.getDataSet().getFile().flush();
            report.getDataSet().getFile().getFD().sync();
        }
//        report.getDataSet().getAnalysisResult().setPathways(null);
    }

    private void addTitleAndDiagram(AnalysisReport report, org.reactome.server.tools.analysis.exporter.playground.model.Pathway pathway, int order) throws Exception {
        report.addNormalTitle(new Paragraph(String.format("2.%s. %s (%s", order + 1, pathway.getName(), pathway.getStId()))
                        .add(PdfUtils.createUrlLinkIcon(report.getDataSet().getIcon(), FontSize.H3, URL.QUERYFORPATHWAYDETAILS + pathway.getStId()))
                        .add(")")
                , FontSize.H3, Indent.I4, pathway.getName());

        // TODO: 29/11/17 add the correct diagram;
        report.addDiagram("R-HSA-163685", report.getDataSet().getReportArgs());
    }

    private void addSummationAndTable(AnalysisReport report, Pathway pathway, PathwayDetail pathwayDetail, Table table) {
        report.addNormalTitle("Summation", FontSize.H4, Indent.I4)
                .addParagraph("species name:" +
                                pathway.getSpeciesName() +
                                (pathwayDetail.getCompartments() != null ? ",compartment name:" + pathwayDetail.getCompartments().get(0).getDisplayName() : "") +
                                (pathway.getIsInDisease() ? ",disease name:" + pathwayDetail.getDiseases().get(0).getDisplayName() : "") +
                                (pathway.getIsInferred() ? ",inferred from:" + pathwayDetail.getEvents().iterator().next().getDisplayName() : "") +
                                (pathwayDetail.getSummations() != null ? "," + pathwayDetail.getSummations().get(0).getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                        , FontSize.H5, 0, MarginLeft.M5);

        report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, Indent.I4)
                .addTable(table);
    }

    private void addCuratorDetail(AnalysisReport report, PathwayDetail pathwayDetail) {
        if (pathwayDetail.getAuthored() != null) {
            addCurator(report, "Authors", PdfUtils.getInstanceEditNames(pathwayDetail.getAuthored()));
        }
        if (pathwayDetail.getEdited() != null) {
            addCurator(report, "Editors", PdfUtils.getInstanceEditNames(pathwayDetail.getEdited()));
        }
        if (pathwayDetail.getModified() != null) {
            addCurator(report, "Reviewers", PdfUtils.getInstanceEditName(pathwayDetail.getModified()));
        }

    }

    private void addLiteratureReference(AnalysisReport report, PathwayDetail pathwayDetail) throws FailToAddLogoException, NullLinkIconDestinationException {
        LiteratureReference literatureReference;
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
                            .add(PdfUtils.createUrlLinkIcon(report.getDataSet().getIcon(), FontSize.H5, literatureReference.getUrl()))
                            .setFontSize(FontSize.H5)
                            .setMarginLeft(MarginLeft.M5)
                    );
                }
            }
        }
    }

    private void addCurator(AnalysisReport report, String title, String content) {
        report.addNormalTitle(title, FontSize.H4, Indent.I4)
                .addParagraph(content, FontSize.H5, Indent.I0, MarginLeft.M5);
    }
}