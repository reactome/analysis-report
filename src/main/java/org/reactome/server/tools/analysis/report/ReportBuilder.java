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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReportBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);

	public ReportBuilder(String ehldPath, String diagramPath, String analysisPath, String fireworksPath, String svgSummary) {
		Locale.setDefault(Locale.ENGLISH);
		DiagramFactory.configure(diagramPath, ehldPath, analysisPath, svgSummary, fireworksPath);
	}

	public void create(AnalysisStoredResult result, String resource, Long species, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream outputStream) throws AnalysisReportException {
		long last = System.nanoTime();
		final AnalysisData analysisData = new AnalysisData(result, resource, species, maxPathways);
		final File tempFolder = new File("temp" + getStamp());
		tempFolder.mkdirs();
		final File latex = new File(tempFolder, "output.tex");
		createTextFile(latex, analysisData);
		final long doc = System.nanoTime() - last;
		last = System.nanoTime();
		DiagramFactory.createFireworksPdf(tempFolder, analysisData, fireworksProfile);
		final long fireworks = System.nanoTime() - last;
		last = System.nanoTime();
		DiagramFactory.createDiagramPdfs(tempFolder, analysisData, diagramProfile, analysisProfile);
		final long diagrams = System.nanoTime() - last;
		last = System.nanoTime();
		copyIcon(tempFolder);
		final long icon = System.nanoTime() - last;
		last = System.nanoTime();
		compile(latex);
		final long compile1 = System.nanoTime() - last;
		last = System.nanoTime();
		compile(latex);
		final long compile2 = System.nanoTime() - last;
		last = System.nanoTime();
		final File file = new File(tempFolder, "output.pdf");
		export(file, outputStream);
		final long copy = System.nanoTime() - last;
		clear(tempFolder);
		System.out.println(Arrays.asList(doc, fireworks, diagrams, icon, compile1, compile2, copy));
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
