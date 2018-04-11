package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.FireworksHelper;
import org.reactome.server.tools.analysis.report.util.PdfUtils;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Fireworks and AnalysisStoredResult.getSummary()
 */
public class Overview implements Section {

	private static final List<String> FIREWORKS_TEXT = PdfUtils.getText(Overview.class.getResourceAsStream("fireworks.txt"));

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		// Starts a new rotated page
		document.getPdfDocument().setDefaultPageSize(PageSize.A4.rotate());
		document.add(new AreaBreak());

		document.add(profile.getH1("3. Genome-wide overview").setDestination("overview"));
		addFireworks(document, analysisData);
		for (String paragraph : FIREWORKS_TEXT)
			document.add(profile.getParagraph(paragraph).setTextAlignment(TextAlignment.CENTER));
		// Starts a new normal page
		document.getPdfDocument().setDefaultPageSize(PageSize.A4);
	}

	private void addFireworks(Document document, AnalysisData analysisData) {
		try {
			final BufferedImage image = FireworksHelper.getFireworks(analysisData);
			final Rectangle area = document.getPdfDocument().getLastPage().getPageSize();
			float width = area.getWidth() - document.getRightMargin() - document.getLeftMargin();
			float height = area.getHeight() - document.getTopMargin() - document.getBottomMargin();
			width *= 0.9;
			height *= .9f;
			final Image fImage = new Image(ImageDataFactory.create(image, Color.WHITE));
			fImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
			fImage.scaleToFit(width, height);
			document.add(fImage);
		} catch (AnalysisServerError | IOException analysisServerError) {
			analysisServerError.printStackTrace();
		}
	}


}
