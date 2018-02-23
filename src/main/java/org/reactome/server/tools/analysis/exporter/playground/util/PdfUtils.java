package org.reactome.server.tools.analysis.exporter.playground.util;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.apache.commons.io.IOUtils;
import org.reactome.server.analysis.core.model.identifier.Identifier;
import org.reactome.server.analysis.core.model.identifier.MainIdentifier;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.graph.domain.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);
    private static ImageData logo;
    private static ImageData linkIcon;
    private static final String LOGO = "images/logo.png";
    private static final String LINKICON = "images/link.png";

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

    public static Image getGotoIcon(String destination) {
        return new Image(linkIcon)
                .scaleToFit(10, 10)
                .setAction(PdfAction.createGoTo(destination));
    }

    /**
     * scale image's size to fit the analysis report's page size.
     *
     * @param image link icon.
     * @param width aim width you want to reach.
     * @return scaled image.
     */
    private static Image ImageAutoScale(Image image, float width) {
        width *= 0.75;//the icon's size will be slightly smaller than the font's size
        float scaling = image.getImageWidth() >= width ? width / image.getImageWidth() : image.getImageWidth() / width;
        image = image.scale(scaling, scaling);
        return image;
    }

    public static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    /**
     * merge the identifiers from all different pathways into a unique array.
     * <p>
     * //     * @param identifierFounds
     *
     * @return {@code Map<Identifier, Set<MainIdentifier>>}
     */
    public static Map<Identifier, Set<MainIdentifier>> getFilteredIdentifiers(AnalysisStoredResult analysisStoredResult, SpeciesFilteredResult speciesFilteredResult, String resource) {
        /*
          in general there just about 1/4 of identifiers was unique(since there have a lot of redundancies)
         */
        Map<Identifier, Set<MainIdentifier>> filteredIdentifiers = new HashMap<>((int) (speciesFilteredResult.getPathways().size() * 0.25));

        PathwayNodeSummary pathwayNodeSummary;
        for (PathwayBase pathway : speciesFilteredResult.getPathways()) {
            pathwayNodeSummary = analysisStoredResult.getPathway(pathway.getStId());
            for (Identifier identifier : pathwayNodeSummary.getData().getIdentifierMap().keySet()) {
//                System.out.println(identifier.getResource().getName()+" "+resource);
                // TODO: 22/02/18 filter identifiers correct
//                if (!identifier.getResource().getName().equals(resource)) {
//                    break;
//                }
                if (!filteredIdentifiers.containsKey(identifier)) {
                    filteredIdentifiers.put(identifier, pathwayNodeSummary.getData().getIdentifierMap().getElements(identifier));
                } else {
                    filteredIdentifiers.get(identifier).addAll(pathwayNodeSummary.getData().getIdentifierMap().getElements(identifier));
                }
            }
        }
        return filteredIdentifiers;
    }

    public static PdfFont createFont(String fontName) throws IOException {
        return PdfFontFactory.createFont(fontName, "", false, false);
    }

    public static Color createColor(String color) {
        return new DeviceRgb(Integer.valueOf(color.substring(1, 3), 16)
                , Integer.valueOf(color.substring(3, 5), 16)
                , Integer.valueOf(color.substring(5, 7), 16));
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

//    public static Paragraph getSummation(List<Summation> summations) {
//        Paragraph paragraph = new Paragraph();
//        for (Summation summation : summations) {
//            Document html = Jsoup.parseBodyFragment(summation.getText());
//            Element body = html.body();
//
//            Elements elements = html.getAllElements();
//            for (Element element : elements) {
//                paragraph.add(element.text());
//            }
//        }
//        return paragraph;
//    }
}
