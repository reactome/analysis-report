package org.reactome.server.tools.analysis.report;

import org.apache.batik.transcoder.TranscoderException;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reactome.server.tools.analysis.exception.AnalysisReportException;
import org.reactome.server.tools.analysis.exception.AnalysisReportRuntimeException;
import org.reactome.server.tools.analysis.report.renderer.ReportRenderer;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonDeserializationException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonNotFoundException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.ehld.exception.EhldException;
import org.reactome.server.tools.diagram.exporter.raster.profiles.ColorProfiles;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class ReportBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
	private static DiagramService diagramService = ReactomeGraphCore.getService(DiagramService.class);
	private final RasterExporter rasterExporter;
	private final FireworksExporter fireworksExporter;
	private AnalysisStoredResult result;
	private String diagramProfile;
	private String analysisProfile;
	private String fireworksProfile;


	public ReportBuilder(String ehldPath, String diagramPath, String analysisPath, String fireworksPath, String svgSummary) {
		rasterExporter = new RasterExporter(diagramPath, ehldPath, analysisPath, svgSummary);
		fireworksExporter = new FireworksExporter(fireworksPath, analysisPath);
	}

	public void create(AnalysisStoredResult result, String resource, Long species, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream outputStream) throws AnalysisReportException {
		this.result = result;
		this.diagramProfile = diagramProfile;
		this.analysisProfile = analysisProfile;
		this.fireworksProfile = fireworksProfile;
		final AnalysisData analysisData = new AnalysisData(result, resource, species, maxPathways);
		final File tempFolder = new File("temp" + getStamp());
		tempFolder.mkdirs();
		final File latex = new File(tempFolder, "output.tex");
		createTextFile(latex, analysisData);
		createSvgs(tempFolder, analysisData);
		compile(latex);
		compile(latex);
		final File file = new File(tempFolder, "output.pdf");
		export(file, outputStream);
		clear(tempFolder);
	}

	private synchronized String getStamp() {
		return "";
//		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS", Locale.ENGLISH));
	}

	private void createSvgs(File folder, AnalysisData data) throws AnalysisReportException {
		try {
			data.getPathways().parallelStream().forEach(pathwayData -> {
				final String stId = pathwayData.getSummary().getStId();
				final DiagramResult result = diagramService.getDiagramResult(stId);
				final RasterArgs args = new RasterArgs(result.getDiagramStId(), "svg");
				args.setSelected(result.getEvents());
				args.setProfiles(new ColorProfiles(diagramProfile, analysisProfile, null));
				args.setResource(data.getResource());
				final File file = new File(folder, stId + ".svg");
				try {
					rasterExporter.export(args, new FileOutputStream(file), this.result);
				} catch (EhldException | AnalysisException | DiagramJsonNotFoundException | IOException | DiagramJsonDeserializationException | TranscoderException e) {
					logger.error("Couldn't create diagram " + args.getStId(), e);
				}
				svgToPdf(file);
			});
		} catch (AnalysisReportRuntimeException e) {
			throw new AnalysisReportException(e);
		}
	}

	private void svgToPdf(File file) throws AnalysisReportRuntimeException {
		final ProcessBuilder builder = new ProcessBuilder("inkscape",
				"--export-area-drawing", "--without-gui",
				"--export-text-to-path",
				"--export-pdf-version=1.5",
				"--file=" + file.getName(),
				"--export-pdf=" + file.getName().replace(".svg", ".pdf"));
		builder.directory(file.getParentFile());
		try {
			final Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new AnalysisReportRuntimeException("Couldn't generate PDF from SVG for " + file.getAbsolutePath(), e);
		}

	}

	private void clear(File folder) {
		final File[] files = folder.listFiles();
		if (files != null)
			for (File file : files)
				if (!file.delete())
					logger.error("Couldn't delete temp file " + file);
		if (!folder.delete())
			logger.error("Couldn't delete temp folder " + folder);
	}

	private void export(File file, OutputStream outputStream) {
		try {
			Files.copy(file.toPath(), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void compile(File latex) {
		final ProcessBuilder builder = new ProcessBuilder("pdflatex",
				"-halt-on-error",
				latex.getAbsolutePath())
				.redirectErrorStream(true)
				.directory(latex.getParentFile());

		try {
			final Process process = builder.start();
			final List<String> lines;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				lines = reader.lines().collect(Collectors.toList());
			}
			final int exitValue = process.waitFor();
			if (exitValue != 0) {
				throw new AnalysisReportRuntimeException("Compilation error for " + latex + System.lineSeparator()
						+ String.join(System.lineSeparator(), lines));
			}
		} catch (IOException | InterruptedException e) {
			throw new AnalysisReportRuntimeException(e);
		}
	}

	private void createTextFile(File latex, AnalysisData analysisData) {
		try (final FileOutputStream latexOS = new FileOutputStream(latex)) {
			final ReportRenderer renderer = new ReportRenderer(latexOS);
			renderer.render(analysisData);
		} catch (IOException e) {
			logger.error("No write permissions in " + latex.getAbsolutePath());
		}
	}
}
