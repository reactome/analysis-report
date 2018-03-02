package org.reactome.server.tools.analysis.exporter.factory;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;

import java.util.List;

/**
 * Renderer use by specifying the paragraph to make the text can be good hyphen
 * in cell space.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ClipTextRenderer extends CellRenderer {
	private List<String> identifiers;

	ClipTextRenderer(Cell cell, List<String> identifiers) {
		super(cell);
		this.identifiers = identifiers;
	}

	@Override
	public LayoutResult layout(LayoutContext layoutContext) {
		Paragraph content = new Paragraph()
				.setFontSize(FontSize.TABLE)
				.setMultipliedLeading(1.0f);

		String[] identifier = this.identifiers.toArray(new String[identifiers.size()]);
		for (int i = 0; i < identifier.length; i++) {
			content.add(new Text(identifier[i].concat(i == identifier.length - 1 ? " " : ", "))
					.setFontSize(FontSize.TABLE));
		}
		this.childRenderers.add(content.createRendererSubTree().setParent(this));
		return super.layout(layoutContext);
	}
}
