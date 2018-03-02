package org.reactome.server.tools.analysis.exporter.util;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.apache.commons.io.IOUtils;
import org.reactome.server.graph.domain.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);
	private static final String LOGO = "images/logo.png";
	private static final String LINKICON = "images/link.png";
	private static ImageData logo;
	private static ImageData linkIcon;

	/*
		Hold the image in memory and avoid iText bug in fail to set goto action in same image.
	 */
	static {
		URL resource = PdfUtils.class.getResource(LOGO);
		logo = ImageDataFactory.create(resource);

		URL linkResource = PdfUtils.class.getResource(LINKICON);
		linkIcon = ImageDataFactory.create(linkResource);
	}

	public static Image getLogo() {
		return new Image(logo);
	}

	public static Image getLinkIcon(String url) {
		return new Image(linkIcon)
				.scaleToFit(10, 10)
				.setAction(PdfAction.createURI(url));
	}

	public static String getTimeStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}

	public static String getAuthorDisplayName(List<Person> authors) {
		StringBuilder name = new StringBuilder();
		int length = authors.size() > 5 ? 5 : authors.size();
		for (int i = 0; i < length; i++) {
			name.append(authors.get(i).getDisplayName().replace(", ", " ")).append(", ");
		}
		if (length == 5) name.append("et al.");
		return name.toString();
	}

	public static List<String> getText(String destination) {
		List<String> text = null;
		try {
			InputStream resource = PdfUtils.class.getResourceAsStream(destination);
			text = IOUtils.readLines(resource, Charset.defaultCharset());
		} catch (IOException e) {
			LOGGER.error("Failed to read text from dictionary : {}", destination);
		}
		return text;
	}
}
