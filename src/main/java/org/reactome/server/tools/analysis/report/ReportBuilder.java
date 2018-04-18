package org.reactome.server.tools.analysis.report;

import org.apache.commons.io.FileUtils;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exception.AnalysisReportException;
import org.reactome.server.tools.analysis.exception.AnalysisReportRuntimeException;
import org.reactome.server.tools.analysis.report.renderer.ReportRenderer;
import org.reactome.server.tools.analysis.report.util.DiagramFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);

	public ReportBuilder(String ehldPath, String diagramPath, String analysisPath, String fireworksPath, String svgSummary) {
		Locale.setDefault(Locale.ENGLISH);
		DiagramFactory.configure(diagramPath, ehldPath, analysisPath, svgSummary, fireworksPath);
	}

	public void create(AnalysisStoredResult result, String resource, Long species, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream outputStream) throws AnalysisReportException {
		DiagramFactory.setProfiles(diagramProfile, analysisProfile, fireworksProfile);
		final AnalysisData analysisData = new AnalysisData(result, resource, species, maxPathways);
		final File tempFolder = new File("temp" + getStamp());
		tempFolder.mkdirs();
		final File latex = new File(tempFolder, "output.tex");
		try {
			Stream.of(
					(Runnable) () -> createTextFile(latex, analysisData),
					() -> DiagramFactory.createFireworksPdf(tempFolder, analysisData),
					() -> DiagramFactory.createDiagramPdfs(tempFolder, analysisData),
					() -> copyIcon(tempFolder)
			).parallel().forEach(Runnable::run);
		} catch (AnalysisReportRuntimeException e) {
			throw new AnalysisReportException(e);
		}
		compile(latex);
		compile(latex);
		final File file = new File(tempFolder, "output.pdf");
		export(file, outputStream);
		clear(tempFolder);
	}

	private synchronized String getStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS", Locale.ENGLISH));
	}

	private void copyIcon(File tempFolder) {
		final File icon = new File(tempFolder, "linkicon.png");
		try {
			FileUtils.copyInputStreamToFile(ReportBuilder.class.getResourceAsStream("link.png"), icon);
		} catch (IOException e) {
			logger.error("Couldn't copy link.png to " + icon);
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
