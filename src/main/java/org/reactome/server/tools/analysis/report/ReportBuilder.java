package org.reactome.server.tools.analysis.report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.IOUtils;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.graph.service.DiagramService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

	public void create(AnalysisStoredResult result, String resource, Long species, int maxPathways, String diagramProfile, String analysisProfile, String fireworksProfile, OutputStream outputStream) {
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
//		clear(tempFolder);

	}

	private synchronized String getStamp() {
		return "";
//		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS", Locale.ENGLISH));
	}

	private void createSvgs(File folder, AnalysisData data) {
		data.getPathways().parallelStream().forEach(pathwayData -> {
			final String stId = pathwayData.getSummary().getStId();
			final DiagramResult result = diagramService.getDiagramResult(stId);
			final RasterArgs args = new RasterArgs(result.getDiagramStId(), "svg");
			args.setSelected(result.getEvents());
			args.setProfiles(new ColorProfiles(diagramProfile, analysisProfile, null));
			args.setResource(data.getResource());
			final File file = new File(folder, stId + ".svg");
			try {
//				synchronized (this) {
					rasterExporter.export(args, new FileOutputStream(file), this.result);
//				}
			} catch (EhldException | AnalysisException | DiagramJsonNotFoundException | IOException | DiagramJsonDeserializationException | TranscoderException e) {
				logger.error("Couldn't create diagram " + args.getStId(), e);
			}
			svgToPdf(file);
		});
	}

	private void svgToPdf(File file) {
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
			e.printStackTrace();
		}
	}

	private void cleanPdf(File file) {
		try {
			final File pdf = new File(file.getParentFile(), file.getName().replace(".svg", ".pdf"));
			final File pdf_tex = new File(file.getParentFile(), file.getName().replace(".svg", ".pdf_tex"));
			PdfDocument document = new PdfDocument(new PdfReader(pdf));
			final int numberOfPages = document.getNumberOfPages();
			document.close();
			final List<String> lines = IOUtils.readLines(new FileInputStream(pdf_tex), Charset.defaultCharset());
			final Pattern pattern = Pattern.compile("\\s*\\\\put\\(\\d+,\\d+\\)\\{\\\\includegraphics\\[width=\\\\unitlength,page=(\\d+)]\\{.*\\.pdf}}%\\s*");
			final List<String> resulting = lines.stream()
					.filter(line -> {
						final Matcher matcher = pattern.matcher(line);
						if (matcher.matches()) {
							final int page = Integer.parseInt(matcher.group(1));
							return page <= numberOfPages;
						}
						return true;
					}).collect(Collectors.toList());
			IOUtils.writeLines(resulting, System.lineSeparator(), new FileOutputStream(pdf_tex), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
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
				latex.getAbsolutePath())
				.redirectErrorStream(true)
				.directory(latex.getParentFile());

		try {
			final Process process = builder.start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				reader.lines().forEach(System.out::println);
			}
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
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
