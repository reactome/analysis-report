package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;
import org.reactome.server.analysis.core.result.model.FoundElements;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.graph.domain.model.*;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.PathwayData;
import org.reactome.server.tools.analysis.exporter.style.Fonts;
import org.reactome.server.tools.analysis.exporter.style.Images;
import org.reactome.server.tools.analysis.exporter.element.*;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.exporter.style.Profile;
import org.reactome.server.tools.analysis.exporter.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.util.HtmlParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Section PathwayDetail contains the detail info for top hit pathways(sorted by
 * p-value), include the overlay diagram image, entities mapped and description
 * for each pathway.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PathwayDetail implements Section {

	private static final String PATHWAY_DETAIL = "https://reactome.org/content/detail/";

	@Override
	public void render(Document document, AnalysisData analysisData) throws AnalysisExporterException {
		document.add(new AreaBreak());
		document.add(new H1("5. Pathway details").setDestination("pathway-details"));
		int i = 1;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final Pathway pathway = pathwayData.getPathway();
			document.add(getLinkedPathwayName(i, pathway));
			final float width = document.getPdfDocument().getLastPage().getMediaBox().getWidth() - document.getLeftMargin() - document.getRightMargin();
			final Image diagram = DiagramHelper.getDiagram(pathway.getStId(), analysisData.getAnalysisStoredResult(), analysisData.getResource(), width);
			if (diagram != null) document.add(diagram);
			addDatabaseObjectList(document, "Compartments", pathway.getCompartment());
			addRelatedDiseases(document, pathway);
			addDatabaseObjectList(document, "Inferred from", pathway.getInferredFrom());

			addSummations(document, pathway);
			addReferences(document, pathway);

//			addEditTable(document, pathway);

			addFoundElements(document, analysisData, pathway);

			document.add(new AreaBreak());
			i += 1;
		}
	}

	private void addFoundElements(Document document, AnalysisData analysisData, Pathway pathway) {
		document.add(new H3("Elements found in this pathway"));
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

	private void addSummations(Document document, Pathway pathway) {
		pathway.getSummation().forEach(summation -> {
			final String[] paragraphs = summation.getText().split("(?i)<br>|<p>");
			for (String paragraph : paragraphs) {
				final String trim = paragraph.trim();
				if (trim.isEmpty()) continue;
				document.add(HtmlParser.parseParagraph(trim));
			}
		});
	}

	private void addRelatedDiseases(Document document, Pathway pathwayDetail) {
		if (pathwayDetail.getDisease() != null) {
			final java.util.List<Disease> diseases = pathwayDetail.getDisease().stream()
					.filter(disease -> !disease.getDisplayName().equals("disease"))
					.collect(Collectors.toList());
			addDatabaseObjectList(document, "Diseases", diseases);
		}
	}

	private String compileName(Person person) {
		if (person.getFirstname() != null && person.getSurname() != null)
			return person.getSurname() + " " + person.getFirstname();
		if (person.getSurname() != null)
			return person.getSurname();
		return person.getFirstname();
	}

	private void addDatabaseObjectList(Document document, String title, Collection<? extends DatabaseObject> objects) {
		if (objects != null && !objects.isEmpty()) {
			final Paragraph paragraph = new P().add(new Text(title + ": ").setFont(Fonts.BOLD));
			final java.util.List<String> list = objects.stream().map(DatabaseObject::getDisplayName).collect(Collectors.toList());
			paragraph.add(new Text(String.join(", ", list)).setFont(Fonts.REGULAR));
			paragraph.add(".");
			document.add(paragraph);
		}
	}

	private Paragraph getLinkedPathwayName(int i, Pathway pathway) {
		return new H2(i + ". " + pathway.getDisplayName())
				.add(" (")
				.add(new Text(pathway.getStId())
						.setAction(PdfAction.createURI(PATHWAY_DETAIL + pathway.getStId()))
						.setFontColor(Profile.REACTOME_COLOR))
				.add(")")
				.setDestination(pathway.getStId());
	}

	private void addIdentifiers(Document document, FoundElements elements, String resource) {
		final Set<FoundEntity> identifiers = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
		identifiers.addAll(elements.getEntities());

		final Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 0.1f, 1, 1, 0.1f, 1, 1}));
		table.useAllAvailableWidth();
		final String input = "Input";
		final String mapping = String.format("%s Id", resource);
		table.addHeaderCell(new HeaderCell(input));
		table.addHeaderCell(new HeaderCell(mapping));
		table.addHeaderCell(new BodyCell(null, 0));
		table.addHeaderCell(new HeaderCell(input));
		table.addHeaderCell(new HeaderCell(mapping));
		table.addHeaderCell(new BodyCell(null, 0));
		table.addHeaderCell(new HeaderCell(input));
		table.addHeaderCell(new HeaderCell(mapping));
		int i = 0;
		int row = 0;
		int column;
		for (FoundEntity identifier : identifiers) {
			column = i % 3;
			row = i / 3;
			final java.util.List<String> mapsTo = identifier.getMapsTo().stream()
					.flatMap(identifierMap -> identifierMap.getIds().stream())
					.collect(Collectors.toList());
			table.addCell(new BodyCell(identifier.getId(), row));
			table.addCell(new BodyCell(String.join(", ", mapsTo), row));
			if (column == 0 || column == 1)
				table.addCell(new BodyCell(null, 0));
			i += 1;
		}
		fillLastRow(table, identifiers.size(), row);
		document.add(table);
	}

	private void fillLastRow(Table table, int identifiers, int row) {
		int n = identifiers % 3;
		if (n == 1) n = 5;
		if (n == 2) n = 2;
		if (n == 3) n = 0;
		for (int j = 0; j < n; j++)
			table.addCell(new BodyCell(null, row));
	}

	private void addReferences(Document document, Pathway pathwayDetail) {
		if (pathwayDetail.getLiteratureReference() != null) {
			document.add(new H3("References"));
			final com.itextpdf.layout.element.List list = new UnorderedList();
			pathwayDetail.getLiteratureReference().stream().limit(5)
					.forEach(publication -> list.add(createPublication(publication)));
			document.add(list);
		}
	}

	private ListItem createPublication(Publication publication) {
		final ListItem item = new ListItem();
		if (publication instanceof LiteratureReference) {
			final LiteratureReference reference = (LiteratureReference) publication;
			item.add(new P(toString(reference)).add(". ").add(Images.getLink(reference.getUrl())));
		} else if (publication instanceof Book) {
			final Book book = (Book) publication;
			item.add(new P(toString(book)).add("."));
		} else if (publication instanceof org.reactome.server.graph.domain.model.URL) {
			final org.reactome.server.graph.domain.model.URL url = (org.reactome.server.graph.domain.model.URL) publication;
			final String citation = toString(url);
			item.add(new P(citation).add(". ").add(Images.getLink(url.getUniformResourceLocator())));
		}
		return item;
	}

	private String toString(LiteratureReference reference) {
		return joinNonNullNonEmpty(", ",
				asString(reference.getAuthor()),
				reference.getTitle(),
				reference.getJournal(),
				reference.getVolume(),
				reference.getYear(),
				reference.getPages(),
				reference.getPubMedIdentifier());
	}

	private String toString(Book book) {
		return joinNonNullNonEmpty(", ",
				asString(book.getAuthor()),
				book.getTitle(),
				book.getChapterTitle(),
				book.getPublisher() == null ? null : book.getPublisher().getDisplayName(),
				book.getYear(),
				book.getPages(),
				book.getISBN());
	}

	private String toString(org.reactome.server.graph.domain.model.URL url) {
		return joinNonNullNonEmpty(", ",
				asString(url.getAuthor()),
				url.getUniformResourceLocator());
	}

	private String joinNonNullNonEmpty(String delimiter, Object... objects) {
		return String.join(delimiter, Stream.of(objects)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList()));
	}

	private String asString(java.util.List<Person> persons) {
		if (persons == null) return "";
		String text = String.join(", ", persons.stream()
				.limit(5)
				.map(this::compileName)
				.collect(Collectors.toList()));
		if (persons.size() > 5) text += " et al.";
		return text;
	}

}
