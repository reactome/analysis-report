package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.graph.domain.model.*;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.document.Command;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.document.TextUtils;
import org.reactome.server.tools.analysis.report.util.HtmlParser;
import org.reactome.server.tools.analysis.report.util.ReferenceFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
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
			final String title = pathway.getDisplayName() + " (\\href{" + link + "}{" + pathway.getStId() + "})";
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

	private void addFoundElements(TexDocument document, AnalysisData analysisData, Pathway pathway) {

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
		document.ln().commandln(TexDocument.SUB_SUB_SECTION + "*", "Edit history");
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

		editions.sort(Comparator.comparing(Edition::getDate));

		final String heaederConf = "\\cellcolor[RGB]{47,158,194}{\\color{white}";
		document.commandln(TexDocument.BEGIN, null, "table", "H")
				.commandln(TexDocument.CENTERING)
				.commandln("bgroup")
				.command("renewcommand").commandln(new Command("arraystretch", "1.5"))
				.commandln(new Command(TexDocument.BEGIN, "tabularx", "\\textwidth", "ccX"))
				.command(new Command(TexDocument.MULTICOLUMN, "1", "c", heaederConf + new Command(TexDocument.TEXT_BF, "Date") + "}")).text(" & ")
				.command(new Command(TexDocument.MULTICOLUMN, "1", "c", heaederConf + new Command(TexDocument.TEXT_BF, "Action") + "}")).text(" & ")
				.command(new Command(TexDocument.MULTICOLUMN, "1", "c", heaederConf + new Command(TexDocument.TEXT_BF, "Authors") + "}")).textln(" \\\\ ");
		for (Edition edition : editions) {
			document.text(TextUtils.scape(edition.getDate())
					+ " & " + TextUtils.scape(edition.getType())
					+ " & " + TextUtils.scape(edition.getAuthors()))
					.textln(" \\\\");
		}
		document.commandln(TexDocument.END, "tabularx")
				.commandln("egroup")
				.commandln(TexDocument.END, "table")
				.ln();
	}

	private class Edition {
		private final String type;
		private final String authors;
		private final String date;

		Edition(String type, InstanceEdit instanceEdit) {
			this.type = type;
			this.authors = asString(instanceEdit.getAuthor());
			this.date = instanceEdit.getDateTime().substring(0, 10);
		}

		public String getType() {
			return type;
		}

		String getAuthors() {
			return authors;
		}

		String getDate() {
			return date;
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

		private String compileName(Person person) {
			if (person.getFirstname() != null && person.getSurname() != null)
				return person.getSurname() + " " + person.getFirstname();
			if (person.getSurname() != null)
				return person.getSurname();
			return person.getFirstname();
		}
	}
}
