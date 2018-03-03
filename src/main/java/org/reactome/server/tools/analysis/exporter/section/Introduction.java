package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.ListItem;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.constant.Images;
import org.reactome.server.tools.analysis.exporter.element.H2;
import org.reactome.server.tools.analysis.exporter.element.LP;
import org.reactome.server.tools.analysis.exporter.element.P;
import org.reactome.server.tools.analysis.exporter.element.UnorderedList;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Section Introduction contains the analysis introduction and Reactome relative
 * publications
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

	private static final List<String> INTRODUCTION = PdfUtils.getText(Introduction.class.getResourceAsStream("introduction.txt"));
	private static final List<Reference> PUBLICATIONS = PdfUtils.getText(Introduction.class.getResourceAsStream("references.txt"))
			.stream()
			.map(s -> s.split("\t"))
			.map(line -> new Reference(line[0], line[1]))
			.collect(Collectors.toList());

	@Override
	public void render(Document document, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(new H2("1. Introduction").setDestination("introduction"));
		INTRODUCTION.stream().map(P::new).forEach(document::add);

		final UnorderedList list = new UnorderedList();
		PUBLICATIONS.forEach(reference -> {
			final Image image = Images.getLink(reference.link);
			ListItem item = new ListItem();
			item.add(new LP(reference.text).add(" ").add(image));
			list.add(item);
		});
		document.add(list);
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
