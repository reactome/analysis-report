package org.reactome.server.tools.analysis.report.util;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonDeserializationException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonNotFoundException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.ehld.exception.EhldException;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Help to create the diagram image by invoking the Reactome {@link
 * RasterExporter}.
 *
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {
	private static final double IMAGE_SCALE = 2;  // 1=keep original ppp
	private static final int MIN_QUALITY = 3;
	private static final Logger logger = LoggerFactory.getLogger(DiagramHelper.class.getName());
	private static RasterExporter exporter;
	private static DiagramService diagramService = ReactomeGraphCore.getService(DiagramService.class);
	private static String diagramProfile;
	private static String analysisProfile;

	public static void insertDiagram(String stId, AnalysisStoredResult result, String resource, Document document) {
		final DiagramResult diagramResult = getDiagramResult(stId);
		final RasterArgs args = new RasterArgs(diagramResult.getDiagramStId(), "pdf");
		args.setSelected(diagramResult.getEvents());
		args.setWriteTitle(false);
		args.setResource(resource);
		args.setProfiles(new ColorProfiles(diagramProfile, analysisProfile, null));
		try {
			final Document imagePdf = exporter.exportToPdf(args, result);
			final PdfFormXObject object = imagePdf.getPdfDocument().getFirstPage().copyAsFormXObject(document.getPdfDocument());
			final float wi = document.getPdfDocument().getLastPage().getPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin() - 0.1f;  // avoid image too large
			final float he = 0.5f * document.getPdfDocument().getLastPage().getPageSize().getHeight() - document.getTopMargin() - document.getBottomMargin();
			document.add(new Image(object).scaleToFit(wi, he).setHorizontalAlignment(HorizontalAlignment.CENTER));
			document.flush();
		} catch (AnalysisException | EhldException | DiagramJsonNotFoundException | DiagramJsonDeserializationException | IOException e) {
			logger.error("Couldn't insert diagram " + stId, e);
		}
	}

	public static void setPaths(String diagramPath, String ehldPath, String analysisPath, String svgSummary) {
		exporter = new RasterExporter(diagramPath, ehldPath, analysisPath, svgSummary);
	}

	public static DiagramResult getDiagramResult(String stId) {
		return diagramService.getDiagramResult(stId);
	}

	public static void setProfiles(String diagramProfile, String analysisProfile) {
		DiagramHelper.diagramProfile = diagramProfile;
		DiagramHelper.analysisProfile = analysisProfile;
	}

}
