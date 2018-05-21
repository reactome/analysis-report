package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.HtmlParser;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Section Introduction contains the analysis introduction and Reactome relative
 * publications
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

	private static final String INTRODUCTION = PdfUtils.getProperty("introduction");
	private static final List<Reference> PUBLICATIONS = PdfUtils.getText(Introduction.class.getResourceAsStream("references.txt"))
			.stream()
			.map(s -> s.split("\t"))
			.map(line -> new Reference(line[0], line[1]))
			.collect(Collectors.toList());

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("Introduction").setDestination("introduction"));
		final Collection<Paragraph> intro = HtmlParser.parseText(profile, INTRODUCTION);
		intro.forEach(document::add);

		final List<Paragraph> paragraphs = new LinkedList<>();
		for (Reference publication : PUBLICATIONS) {
			final Image image = Images.getLink(publication.link, profile.getFontSize());
			paragraphs.add(profile.getParagraph(publication.text).add(" ").add(image));
		}
		document.add(profile.getList(paragraphs));
	}

	private static class Reference {
		String text;
		String link;

		Reference(String text, String link) {
			this.text = text;
			this.link = link;
		}
	}
}
