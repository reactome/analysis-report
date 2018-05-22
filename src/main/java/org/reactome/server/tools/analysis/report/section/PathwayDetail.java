package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.analysis.core.result.model.FoundElements;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.graph.domain.model.*;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.ApaStyle;
import org.reactome.server.tools.analysis.report.util.DiagramHelper;
import org.reactome.server.tools.analysis.report.util.HtmlParser;

import java.util.*;
import java.util.stream.Collectors;

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
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("Pathway details").setDestination("pathway-details"));
		int i = 1;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final Pathway pathway = pathwayData.getPathway();
			document.add(getTitle(profile, i, pathway));
			DiagramHelper.insertDiagram(pathway.getStId(), analysisData.getAnalysisStoredResult(), analysisData.getResource(), document);
			addDatabaseObjectList(document, "Cellular compartments", pathway.getCompartment(), profile);
			addRelatedDiseases(document, pathway, profile);
			addDatabaseObjectList(document, "Inferred from", pathway.getInferredFrom(), profile);

			addSummations(document, pathway, profile);
			addReferences(document, pathway, profile);

			addEditTable(document, pathway, profile);

			addFoundElements(document, analysisData, pathway, profile);

			document.add(new AreaBreak());
			document.flush();
			i += 1;
		}
	}

	private List getTitle(PdfProfile profile, int i, Pathway pathway) {
		final List list = new List(ListNumberingType.DECIMAL)
				.setItemStartIndex(i)
				.setFontSize(profile.getFontSize() + 2)
				.setBold()
				.setSymbolIndent(10);
		final ListItem item = new ListItem();
		final Paragraph paragraph = new Paragraph(pathway.getDisplayName())
				.add(" (")
				.add(new Text(pathway.getStId())
						.setAction(PdfAction.createURI(PATHWAY_DETAIL + pathway.getStId()))
						.setFontColor(profile.getLinkColor()))
				.add(")")
				.setDestination(pathway.getStId());
		item.add(paragraph);
		list.add(item);
		return list;
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
		pathway.getSummation().stream()
				.map(summation -> HtmlParser.parseText(profile, summation.getText()))
				.flatMap(Collection::stream)
				.forEach(document::add);
	}

	private void addRelatedDiseases(Document document, Pathway pathwayDetail, PdfProfile profile) {
		if (pathwayDetail.getDisease() != null) {
			final java.util.List<Disease> diseases = pathwayDetail.getDisease().stream()
					.filter(disease -> !disease.getDisplayName().equals("disease"))
					.collect(Collectors.toList());
			addDatabaseObjectList(document, "Diseases", diseases, profile);
		}
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

	private void addIdentifiers(Document document, FoundElements elements, String resource, PdfProfile profile) {
		if (elements.getExpNames() == null || elements.getExpNames().isEmpty())
			addSimpleTable(document, elements, resource, profile);
		else addExpressionTable(document, elements, resource, profile);
	}

	private void addExpressionTable(Document document, FoundElements elements, String resource, PdfProfile profile) {
		final Table expressionTable = Tables.getExpressionTable(elements.getEntities(), resource, profile, elements.getExpNames());
		document.add(expressionTable);
	}

	private void addSimpleTable(Document document, FoundElements elements, String resource, PdfProfile profile) {
		final Table identifiersTable = Tables.getIdentifiersTable(elements.getEntities(), resource, profile);
		document.add(identifiersTable);
	}

	private void addReferences(Document document, Pathway pathwayDetail, PdfProfile profile) {
		if (pathwayDetail.getLiteratureReference() != null) {
			document.add(profile.getH3("References"));
			pathwayDetail.getLiteratureReference().stream()
					.limit(5)
					.map(publication -> createPublication(publication, profile))
					.forEach(document::add);
		}
	}

	private Paragraph createPublication(Publication publication, PdfProfile profile) {
		final java.util.List<Text> texts = ApaStyle.toApa(publication);
		final Paragraph paragraph = profile.getParagraph("")
				.setFirstLineIndent(-15)
				.setPaddingLeft(15)
				.setMultipliedLeading(1);
		texts.forEach(paragraph::add);
		if (publication instanceof LiteratureReference) {
			final LiteratureReference reference = (LiteratureReference) publication;
			if (reference.getUrl() != null)
				paragraph.add(" ").add(Images.getLink(reference.getUrl(), profile.getFontSize()));
		} else if (publication instanceof org.reactome.server.graph.domain.model.URL) {
			final org.reactome.server.graph.domain.model.URL url = (org.reactome.server.graph.domain.model.URL) publication;
			paragraph.add(Images.getLink(url.getUniformResourceLocator(), profile.getFontSize() * 0.8f));
		}
		return paragraph;
	}

	private String asString(Collection<Person> persons) {
		return asString(persons, 5);
	}

	private String asString(Collection<Person> persons, int maxAuthors) {
		if (persons == null) return "";
		String text = String.join(", ", persons.stream()
				.limit(maxAuthors)
				.map(this::compileName)
				.collect(Collectors.toList()));
		if (persons.size() > maxAuthors) text += " et al.";
		return text;
	}

	private String compileName(Person person) {
		if (person.getFirstname() != null && person.getSurname() != null)
			return person.getSurname() + " " + initials(person.getFirstname());
		if (person.getSurname() != null)
			return person.getSurname();
		return person.getFirstname();
	}

	private String initials(String name) {
		return Arrays.stream(name.split(" "))
				.map(n -> n.substring(0, 1).toUpperCase())
				.collect(Collectors.joining(" "));
	}

	private void addEditTable(Document document, Pathway pathway, PdfProfile profile) {
		document.add(profile.getH3("Edit history"));
		final java.util.List<Edition> editions = new LinkedList<>();
		if (pathway.getCreated() != null)
			editions.add(new Edition("Created", pathway.getCreated()));
		if (pathway.getModified() != null)
			editions.add(new Edition("Modified", pathway.getModified()));
		if (pathway.getAuthored() != null)
			pathway.getAuthored().forEach(instanceEdit -> editions.add(new Edition("Authored", instanceEdit)));
		if (pathway.getEdited() != null)
			pathway.getEdited().forEach(instanceEdit -> editions.add(new Edition("Edited", instanceEdit)));
		if (pathway.getReviewed() != null)
			pathway.getReviewed().forEach(instanceEdit -> editions.add(new Edition("Reviewed", instanceEdit)));
		if (pathway.getRevised() != null)
			pathway.getRevised().forEach(instanceEdit -> editions.add(new Edition("Revised", instanceEdit)));

		// Group by date and type
		final Map<String, Map<String, java.util.List<Edition>>> edits = editions.stream()
				.collect(Collectors.groupingBy(Edition::getDate,
						TreeMap::new,  // forces key sorting
						Collectors.groupingBy(Edition::getType)));

		final Table table = new Table(new float[]{0.2f, 0.2f, 1f});
		table.useAllAvailableWidth();
		table.setBorder(Border.NO_BORDER);
		table.addHeaderCell(profile.getHeaderCell("Date"));
		table.addHeaderCell(profile.getHeaderCell("Action"));
		table.addHeaderCell(profile.getHeaderCell("Author"));
		int row = 0;
		for (Map.Entry<String, Map<String, java.util.List<Edition>>> dateEntry : edits.entrySet()) {
			for (Map.Entry<String, java.util.List<Edition>> typeEntry : dateEntry.getValue().entrySet()) {
				table.addCell(profile.getBodyCell(dateEntry.getKey(), row));
				table.addCell(profile.getBodyCell(typeEntry.getKey(), row));
				final Set<Person> authors = typeEntry.getValue().stream()
						.map(Edition::getAuthors)
						.filter(Objects::nonNull)
						.flatMap(Collection::stream)
						.collect(Collectors.toSet());
				table.addCell(profile.getBodyCell(asString(authors), row).setTextAlignment(TextAlignment.LEFT).setPadding(5));
				row += 1;
			}
		}
		document.add(table);

	}

	private class Edition {
		private final String type;
		private final java.util.List<Person> authors;
		private final String date;

		Edition(String type, InstanceEdit instanceEdit) {
			this.type = type;
			this.authors = instanceEdit.getAuthor();
			this.date = instanceEdit.getDateTime().substring(0, 10);
		}

		public String getType() {
			return type;
		}

		java.util.List<Person> getAuthors() {
			return authors;
		}

		String getDate() {
			return date;
		}
	}
}
