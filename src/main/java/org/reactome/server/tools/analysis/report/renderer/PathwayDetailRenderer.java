package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.analysis.core.result.model.FoundElements;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierMap;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.graph.domain.model.DatabaseObject;
import org.reactome.server.graph.domain.model.Disease;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.document.TextUtils;
import org.reactome.server.tools.analysis.report.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class PathwayDetailRenderer implements TexRenderer {

	private static final DiagramService SERVICE = ReactomeGraphCore.getService(DiagramService.class);
	private static final String PATHWAY_DETAIL = "https://reactome.org/content/detail/";

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(TexDocument.SECTION, "Pathway details");
		analysisData.getPathways().forEach(pathwayData -> {
			final Pathway pathway = pathwayData.getPathway();
			final String link = PATHWAY_DETAIL + pathway.getStId();
			final String title = TextUtils.scape(pathway.getDisplayName()) + " (\\href{" + link + "}{" + pathway.getStId() + "})";
			document.commandln(TexDocument.SUB_SECTION, title);
			addDiagram(document, pathwayData);
			addDatabaseObjectList(document, "Cellular compartments", pathway.getCompartment());
			addRelatedDiseases(document, pathway);
			addDatabaseObjectList(document, "Inferred from", pathway.getInferredFrom());

			addSummations(document, pathway);
			addReferences(document, pathway);
			addEditTable(document, pathway);
			addFoundElements(document, analysisData, pathway);

			document.commandln(TexDocument.NEW_PAGE);
		});
	}

	private void addDiagram(TexDocument document, PathwayData pathwayData) {
		final DiagramResult result = SERVICE.getDiagramResult(pathwayData.getSummary().getStId());
		// 210mm - 20mm - 25mm = 165mm
		final int width = (int) (Math.min(result.getWidth(), 165));
		document.commandln(TexDocument.BEGIN, null, TexDocument.FIGURE, "H")
				.commandln(TexDocument.CENTERING)
//				.command("def").commandln("svgwidth", width + "mm")
//				.commandln("input", pathwayData.getSummary().getStId() + ".pdf_tex")
				.commandln(TexDocument.INCLUDE_GRAPHICS, "width=" + width + "mm", pathwayData.getSummary().getStId())
				.commandln(TexDocument.END, TexDocument.FIGURE)
				.ln();
	}

	private void addRelatedDiseases(TexDocument document, Pathway pathwayDetail) {
		if (pathwayDetail.getDisease() != null) {
			final java.util.List<Disease> diseases = pathwayDetail.getDisease().stream()
					.filter(disease -> !disease.getDisplayName().equals("disease"))
					.collect(Collectors.toList());
			addDatabaseObjectList(document, "Diseases", diseases);
		}
	}

	private void addDatabaseObjectList(TexDocument document, String title, Collection<? extends DatabaseObject> objects) {
		if (objects != null && !objects.isEmpty()) {
			final java.util.List<String> list = objects.stream()
					.map(DatabaseObject::getDisplayName)
					.collect(Collectors.toList());
			final String body = String.join(", ", list) + ".";
			document.command(TexDocument.TEXT_BF, title + ": ")
					.paragraph(body);
		}
	}

	private void addSummations(TexDocument document, Pathway pathway) {
		pathway.getSummation().forEach(summation -> HtmlParser.parseText(document, summation));
	}

	private void addReferences(TexDocument document, Pathway pathway) {
		if (pathway.getLiteratureReference() != null) {
			document.commandln(TexDocument.SUB_SUB_SECTION + "*", "References");
			document.commandln(TexDocument.BEGIN, "itemize");
			pathway.getLiteratureReference().stream()
					.limit(5)
					.map(ReferenceFactory::createPublication)
					.forEach(reference -> document.command("item").textln(" " + reference));
			document.commandln(TexDocument.END, "itemize");
		}
	}

	private void addEditTable(TexDocument document, Pathway pathway) {
		final List<List<String>> rows = Editions.from(pathway).stream()
				.map(edition -> Arrays.asList(edition.getDate(), edition.getType(), edition.getAuthors()))
				.collect(Collectors.toList());
		final TexTable table = new TexTable(Arrays.asList("Date", "Action", "Author"), rows);
		table.setAlignment("ccZ");
		document.ln().commandln(TexDocument.SUB_SUB_SECTION + "*", "Edit history");
		table.render(document);
	}


	private void addFoundElements(TexDocument document, AnalysisData analysisData, Pathway pathway) {
		document.commandln(TexDocument.SUB_SUB_SECTION + "*", "Elements found in this pathway");
		if (analysisData.getResource().equalsIgnoreCase("total")) {
			for (ResourceSummary summary : analysisData.getAnalysisStoredResult().getResourceSummary()) {
				if (summary.getResource().equalsIgnoreCase("total")) continue;
				final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathway.getStId(), summary.getResource());
				if (foundElements.getFoundEntities() > 0)
					addIdentifiers(document, foundElements, analysisData.beautify(summary.getResource()));
			}
		} else {
			final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathway.getStId(), analysisData.getResource());
			addIdentifiers(document, foundElements, analysisData.getBeautifiedResource());
		}


	}

	private void addIdentifiers(TexDocument document, FoundElements elements, String resource) {
		if (elements.getExpNames() == null || elements.getExpNames().isEmpty())
			addSimpleTable(document, elements, resource);
		else addExpressionTable(document, elements, resource);

	}

	private void addExpressionTable(TexDocument document, FoundElements elements, String resource) {
		final List<String> expNames = elements.getExpNames();
		final int numberOfColumns = Math.min(5, expNames.size());
		final List<String> headers = new LinkedList<>(Arrays.asList("Input", resource + " Id"));
		for (int i = 0; i < numberOfColumns; i++) headers.add(TextUtils.ellipsis(expNames.get(i)));

		final List<List<String>> rows = new LinkedList<>();
		for (FoundEntity entity : elements.getEntities()) {
			final LinkedList<String> row = new LinkedList<>(Arrays.asList(entity.getId(), toString(entity.getMapsTo())));
			for (int i = 0; i < numberOfColumns; i++)
				row.add(PdfUtils.formatNumber(entity.getExp().get(i)));
			rows.add(row);
		}
		final TexTable table = new TexTable(headers, rows);
		final StringBuilder builder = new StringBuilder("cZ");
		for (int i = 0; i < numberOfColumns; i++) builder.append("c");
		table.setAlignment(builder.toString());
		table.render(document);
		//		table.addHeaderCell(profile.getHeaderCell("Input"));
//		table.addHeaderCell(profile.getHeaderCell(resource + " Id"));
//		for (int i = 0; i < rows; i++)
//			table.addHeaderCell(profile.getHeaderCell(expNames.get(i)));
//		int row = 0;
//		for (FoundEntity entity : elements.getEntities()) {
//			table.addCell(profile.getBodyCell(entity.getId(), row));
//			table.addCell(profile.getBodyCell(toString(entity.getMapsTo()), row));
//			for (int i = 0; i < rows; i++) {
//				table.addCell(profile.getBodyCell(PdfUtils.formatNumber(entity.getExp().get(i)), row));
//			}
//			row++;
//		}
//		document.add(table);
	}

	private void addSimpleTable(TexDocument document, FoundElements elements, String resource) {
//		// This is a custom made layout to present 3 tables side by side
//		final java.util.List<FoundEntity> identifiers = elements.getEntities().stream()
//				.sorted(Comparator.comparing(IdentifierSummary::getId))
//				.distinct()
//				.collect(Collectors.toList());
//		final Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 0.1f, 1, 1, 0.1f, 1, 1}));
//		table.useAllAvailableWidth();
//		final String input = "Input";
//		final String mapping = String.format("%s Id", resource);
//		table.addHeaderCell(profile.getHeaderCell(input));
//		table.addHeaderCell(profile.getHeaderCell(mapping));
//		table.addHeaderCell(profile.getBodyCell("", 0));
//		table.addHeaderCell(profile.getHeaderCell(input));
//		table.addHeaderCell(profile.getHeaderCell(mapping));
//		table.addHeaderCell(profile.getBodyCell("", 0));
//		table.addHeaderCell(profile.getHeaderCell(input));
//		table.addHeaderCell(profile.getHeaderCell(mapping));
//		int i = 0;
//		int row = 0;
//		int column;
//		for (FoundEntity identifier : identifiers) {
//			column = i % 3;
//			row = i / 3;
//			final String join = toString(identifier.getMapsTo());
//			table.addCell(profile.getBodyCell(identifier.getId(), row));
//			table.addCell(profile.getBodyCell(join, row));
//			if (column == 0 || column == 1)
//				table.addCell(profile.getBodyCell("", 0));
//			i += 1;
//		}
//		document.add(table);
	}

	private String toString(Set<IdentifierMap> identifier) {
		final java.util.List<String> mapsTo = identifier.stream()
				.flatMap(identifierMap -> identifierMap.getIds().stream())
				.collect(Collectors.toList());
		return String.join(", ", mapsTo);
	}

}
