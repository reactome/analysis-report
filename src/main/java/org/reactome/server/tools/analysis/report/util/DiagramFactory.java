package org.reactome.server.tools.analysis.report.util;

import org.apache.batik.transcoder.TranscoderException;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonDeserializationException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonNotFoundException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.ehld.exception.EhldException;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

public class DiagramFactory {

	private static final Logger logger = LoggerFactory.getLogger(DiagramFactory.class);
	private static DiagramService diagramService = ReactomeGraphCore.getService(DiagramService.class);
	private static RasterExporter diagramExporter;
	private static FireworksExporter fireworksExporter;
	private static String diagramProfile;
	private static String analysisProfile;
	private static String fireworksProfile;


	public static void createDiagramPdfs(File folder, AnalysisData data) {
		data.getPathways().parallelStream()
				.forEach(pathwayData -> createDiagramPdf(folder, data, pathwayData.getSummary().getStId()));
	}

	private static void createDiagramPdf(File folder, AnalysisData data, String stId) {
		// We will copy the SVG directly from memory into inkscape command
		// We start the inkscape in a thread waiting for /dev/stdin
		// Then we pipe the SVG into /dev/stdin through process.getOutputStream()
		// Ah, and it's thread safe, since linux 'creates' a new /dev/stdin per
		// process
		final DiagramResult result = diagramService.getDiagramResult(stId);
		final RasterArgs args = new RasterArgs(result.getDiagramStId(), "svg");
		args.setSelected(result.getEvents());
		args.setProfiles(new ColorProfiles(diagramProfile, analysisProfile, null));
		args.setResource(data.getResource());
		final ProcessBuilder builder = new ProcessBuilder("inkscape",
				"--export-area-drawing", "--without-gui",
				"--export-pdf-version=1.5",
				"--file=" + "/dev/stdin",
				"--export-pdf=" + stId + ".pdf")
				.directory(folder);
		try {
			final Process process = builder.start();
			diagramExporter.export(args, new BufferedOutputStream(process.getOutputStream()), data.getAnalysisStoredResult());
			process.getOutputStream().close();
			process.waitFor();
		} catch (InterruptedException | IOException | TranscoderException | DiagramJsonNotFoundException | EhldException | DiagramJsonDeserializationException | AnalysisException e) {
			logger.error("Couldn't create diagram " + args.getStId(), e);
		}
	}

	public static void configure(String diagramPath, String ehldPath, String analysisPath, String svgSummary, String fireworksPath) {
		diagramExporter = new RasterExporter(diagramPath, ehldPath, analysisPath, svgSummary);
		fireworksExporter = new FireworksExporter(fireworksPath, analysisPath);
	}

	public static void createFireworksPdf(File folder, AnalysisData data) {
		final String species = data.getSpecies().replaceAll(" ", "_");
		final FireworkArgs args = new FireworkArgs(species, "svg");
		args.setProfile(fireworksProfile);
		final ProcessBuilder builder = new ProcessBuilder("inkscape",
				"--export-area-drawing", "--without-gui",
				"--export-pdf-version=1.5",
				"--file=" + "/dev/stdin",
				"--export-pdf=" + species + ".pdf")
				.directory(folder);
		try {
			final Process process = builder.start();
			fireworksExporter.render(args, data.getAnalysisStoredResult(), new BufferedOutputStream(process.getOutputStream()));
			process.getOutputStream().close();
			process.waitFor();
		} catch (AnalysisServerError | TranscoderException | IOException | InterruptedException e) {
			logger.error("Couldn't create fireworks " + args.getSpeciesName(), e);
		}
	}

	public static void setProfiles(String diagramProfile, String analysisProfile, String fireworksProfile) {
		DiagramFactory.diagramProfile = diagramProfile;
		DiagramFactory.analysisProfile = analysisProfile;
		DiagramFactory.fireworksProfile = fireworksProfile;
	}
}
