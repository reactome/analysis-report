package org.reactome.server.tools.analysis.exporter.playground.util;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.apache.commons.io.IOUtils;
import org.reactome.server.analysis.core.model.identifier.Identifier;
import org.reactome.server.analysis.core.model.identifier.MainIdentifier;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.graph.domain.model.InstanceEdit;
import org.reactome.server.graph.domain.model.Person;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public static Image createImage(String fileName) throws FailToAddLogoException {
        try {
            return new Image(ImageDataFactory.create(fileName));
        } catch (MalformedURLException e) {
            throw new FailToAddLogoException("Failed to add image in pdf file", e);
        }
    }

    public static Image createGoToLinkIcon(Image icon, float width, String destination) throws NullLinkIconDestinationException {
        if (null == destination)
            throw new NullLinkIconDestinationException("Link icon's destination should not be null!");
        return ImageAutoScale(icon, width).setAction(PdfAction.createGoTo(destination));
    }

    public static Image createUrlLinkIcon(Image icon, float width, String url) throws NullLinkIconDestinationException {
        if (null == url) throw new NullLinkIconDestinationException("Link icon's url should not be null!");
        return ImageAutoScale(icon, width).setAction(PdfAction.createURI(url));
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
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    /**
     * merge the identifiers from all different pathways into a unique array.
     * <p>
     * //     * @param identifierFounds
     *
     * @return
     */
    public static Map<Identifier, Set<MainIdentifier>> getFilteredIdentifiers(List<PathwayNodeSummary> pathways) {
        /*
          in general there just about 1/4 of identifiers was unique(since there have a lot of redundancies)
         */
        Map<Identifier, Set<MainIdentifier>> filteredIdentifiers = new HashMap<>((int) (pathways.size() * 0.25));

        for (PathwayNodeSummary pathway : pathways) {
            for (Identifier identifier : pathway.getData().getIdentifierMap().keySet()) {
                if (!filteredIdentifiers.containsKey(identifier)) {
                    filteredIdentifiers.put(identifier, pathway.getData().getIdentifierMap().getElements(identifier));
                } else {
                    filteredIdentifiers.get(identifier).addAll(pathway.getData().getIdentifierMap().getElements(identifier));
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


    public static String getInstanceEditName(InstanceEdit instanceEdit) {
        StringBuilder name = new StringBuilder();
//        if (instanceEdit.getAuthor() != null) {
        instanceEdit.getAuthor().forEach(person -> name.append(person.getSurname())
                .append(' ')
                .append(person.getFirstname()).append(',')
                .append(instanceEdit.getDateTime().substring(0, 10))
                .append("\r\n"));
        return name.toString();
//        } else {
//            return null;
//        }
    }

    public static String getAuthorDisplayName(List<Person> authors) {
        StringBuilder name = new StringBuilder();
        authors.forEach(person -> name.append(person.getDisplayName().replace(", ", " ")).append(','));
        return name.toString();
    }

    public static String getInstanceEditNames(List<InstanceEdit> curators) {
        StringBuilder names = new StringBuilder();
        curators.forEach(instanceEdit -> names.append(PdfUtils.getInstanceEditName(instanceEdit)));
        return names.toString();
    }

    public static String[] getText(String destination) {
        String[] texts = null;
        try {
            texts = IOUtils.toString(new FileReader(destination)).split(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (IOException e) {
            LOGGER.error("Failed to read text from dictionary : {}", destination);
        }
        return texts;
    }

    public static PageSize createPageSize(String type) throws NoSuchPageSizeException {
        switch (type) {
            case "A0":
                return PageSize.A0;
            case "A1":
                return PageSize.A1;
            case "A2":
                return PageSize.A2;
            case "A3":
                return PageSize.A3;
            case "A4":
                return PageSize.A4;
            case "A5":
                return PageSize.A5;
            case "A6":
                return PageSize.A6;
            case "A7":
                return PageSize.A7;
            case "A8":
                return PageSize.A8;
            case "A9":
                return PageSize.A9;
            case "A10":
                return PageSize.A10;
            case "B0":
                return PageSize.B0;
            case "B1":
                return PageSize.B1;
            case "B2":
                return PageSize.B2;
            case "B3":
                return PageSize.B3;
            case "B4":
                return PageSize.B4;
            case "B5":
                return PageSize.B5;
            case "B6":
                return PageSize.B6;
            case "B7":
                return PageSize.B7;
            case "B8":
                return PageSize.B8;
            case "B9":
                return PageSize.B9;
            case "B10":
                return PageSize.B10;
            default:
                throw new NoSuchPageSizeException(String.format("Cant recognize page size : %s", type));
        }
    }
}
