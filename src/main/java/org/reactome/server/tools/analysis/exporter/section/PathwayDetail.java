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
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.exporter.style.Images;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;
import org.reactome.server.tools.analysis.exporter.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.util.HtmlParser;

import java.util.*;
import java.util.List;
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
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) throws AnalysisExporterException {
		document.add(new AreaBreak());
		document.add(profile.getH1("5. Pathway details").setDestination("pathway-details"));
		int i = 1;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final Pathway pathway = pathwayData.getPathway();
			document.add(getLinkedPathwayName(i, pathway, profile));
			final float width = document.getPdfDocument().getLastPage().getMediaBox().getWidth() - document.getLeftMargin() - document.getRightMargin();
			final Image diagram = DiagramHelper.getDiagram(pathway.getStId(), analysisData.getAnalysisStoredResult(), analysisData.getResource(), width);
			if (diagram != null) document.add(diagram);
			addDatabaseObjectList(document, "Compartments", pathway.getCompartment(), profile);
			addRelatedDiseases(document, pathway, profile);
			addDatabaseObjectList(document, "Inferred from", pathway.getInferredFrom(), profile);

			addSummations(document, pathway, profile);
			addReferences(document, pathway, profile);

//			addEditTable(document, pathway);

			addFoundElements(document, analysisData, pathway, profile);

			document.add(new AreaBreak());
			i += 1;
		}
	}

	private void addFoundElements(Document document, AnalysisData analysisData, Pathway pathway, PdfProfile profile) {
		document.add(profile.getH3("Elements found in this pathway"));
		if (analysisData.getResource().equalsIgnoreCase("total")) {
			for (ResourceSummary summary : analysisData.getAnalysisStoredResult().getResourceSummary()) {
				if (summary.getResource().equalsIgnoreCase("total")) continue;
				final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathway.getStId(), summary.getResource());
				if (foundElements.getFoundEntities() > 0)
					addIdentifiers(document, foundElements, analysisData.beautify(summary.getResource()), profile);
			}
		} else {
			final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathway.getStId(), analysisData.getResource());
			addIdentifiers(document, foundElements, analysisData.getBeautifiedResource(), profile);
		}
	}

	private void addSummations(Document document, Pathway pathway, PdfProfile profile) {
		pathway.getSummation().forEach(summation -> {
			final String[] paragraphs = summation.getText().split("(?i)<br>|<p>");
			for (String paragraph : paragraphs) {
				final String trim = paragraph.trim();
				if (trim.isEmpty()) continue;
				document.add(HtmlParser.parseParagraph(trim, profile));
			}
		});
	}

	private void addRelatedDiseases(Document document, Pathway pathwayDetail, PdfProfile profile) {
		if (pathwayDetail.getDisease() != null) {
			final java.util.List<Disease> diseases = pathwayDetail.getDisease().stream()
					.filter(disease -> !disease.getDisplayName().equals("disease"))
					.collect(Collectors.toList());
			addDatabaseObjectList(document, "Diseases", diseases, profile);
		}
	}

	private String compileName(Person person) {
		if (person.getFirstname() != null && person.getSurname() != null)
			return person.getSurname() + " " + person.getFirstname();
		if (person.getSurname() != null)
			return person.getSurname();
		return person.getFirstname();
	}

	private void addDatabaseObjectList(Document document, String title, Collection<? extends DatabaseObject> objects, PdfProfile profile) {
		if (objects != null && !objects.isEmpty()) {
			final java.util.List<String> list = objects.stream()
					.map(DatabaseObject::getDisplayName)
					.collect(Collectors.toList());
			final String body = String.join(", ", list) + ".";
			final Paragraph paragraph = profile.getParagraph("")
					.add(new Text(title + ": ").setFont(profile.getBoldFont()))
					.add(body);
			document.add(paragraph);
		}
	}

	private Paragraph getLinkedPathwayName(int i, Pathway pathway, PdfProfile profile) {
		return profile.getH2(i + ". " + pathway.getDisplayName())
				.add(" (")
				.add(new Text(pathway.getStId())
						.setAction(PdfAction.createURI(PATHWAY_DETAIL + pathway.getStId()))
						.setFontColor(profile.getLinkColor()))
				.add(")")
				.setDestination(pathway.getStId());
	}

	private void addIdentifiers(Document document, FoundElements elements, String resource, PdfProfile profile) {
		final Set<FoundEntity> identifiers = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
		identifiers.addAll(elements.getEntities());

		final Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 0.1f, 1, 1, 0.1f, 1, 1}));
		table.useAllAvailableWidth();
		final String input = "Input";
		final String mapping = String.format("%s Id", resource);
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		table.addHeaderCell(profile.getBodyCell(null, 0));
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		table.addHeaderCell(profile.getBodyCell(null, 0));
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		int i = 0;
		int row = 0;
		int column;
		for (FoundEntity identifier : identifiers) {
			column = i % 3;
			row = i / 3;
			final java.util.List<String> mapsTo = identifier.getMapsTo().stream()
					.flatMap(identifierMap -> identifierMap.getIds().stream())
					.collect(Collectors.toList());
			table.addCell(profile.getBodyCell(identifier.getId(), row));
			table.addCell(profile.getBodyCell(String.join(", ", mapsTo), row));
			if (column == 0 || column == 1)
				table.addCell(profile.getBodyCell(null, 0));
			i += 1;
		}
		fillLastRow(table, identifiers.size(), row, profile);
		document.add(table);
	}

	private void fillLastRow(Table table, int identifiers, int row, PdfProfile profile) {
		int n = identifiers % 3;
		if (n == 1) n = 5;
		if (n == 2) n = 2;
		if (n == 3) n = 0;
		for (int j = 0; j < n; j++)
			table.addCell(profile.getBodyCell(null, row));
	}

	private void addReferences(Document document, Pathway pathwayDetail, PdfProfile profile) {
		if (pathwayDetail.getLiteratureReference() != null) {
			document.add(profile.getH3("References"));
			final List<Paragraph> paragraphs = pathwayDetail.getLiteratureReference().stream()
					.limit(5)
					.map(publication -> createPublication(publication, profile))
					.collect(Collectors.toList());
			document.add(profile.getList(paragraphs));
		}
	}

	private Paragraph createPublication(Publication publication, PdfProfile profile) {
		if (publication instanceof LiteratureReference) {
			final LiteratureReference reference = (LiteratureReference) publication;
			return profile.getParagraph(toString(reference))
					.add(". ")
					.add(Images.getLink(reference.getUrl(), profile.getP()));
		} else if (publication instanceof Book) {
			final Book book = (Book) publication;
			return profile.getParagraph(toString(book)).add(".");
		} else if (publication instanceof org.reactome.server.graph.domain.model.URL) {
			final org.reactome.server.graph.domain.model.URL url = (org.reactome.server.graph.domain.model.URL) publication;
			final String citation = toString(url);
			return profile.getParagraph(citation).add(". ").add(Images.getLink(url.getUniformResourceLocator(), profile.getP()));
		}
		return profile.getParagraph(publication.getDisplayName());
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
