package org.reactome.server.tools.analysis.exporter.factory;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.TableRenderer;
import org.reactome.server.tools.analysis.exporter.constant.Colors;

/**
 * Render the cell's background color. <p>line number is zero-based, the odd
 * line's background is white, and the even line is {@link Colors}.GRAY and so
 * on.
 * <p>
 * Usage: {@code table.setNextRenderer(new BackgroundColorRenderer(table));}
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
class BackgroundColorRenderer extends TableRenderer {
	/**
	 * @param table table need to be rendered.
	 */
	BackgroundColorRenderer(Table table) {
		super(table);
		table.setBorder(Border.NO_BORDER)
				.setWidth(UnitValue.createPercentValue(100));
		int rows = table.getNumberOfRows();
		int columns = table.getNumberOfColumns();
		for (int row = 0; row < rows; row++) {
			if (row % 2 == 1) {
				for (int column = 0; column < columns; column++) {
					table.getCell(row, column).setBackgroundColor(Colors.GRAY);
				}
			}
		}
	}
}
