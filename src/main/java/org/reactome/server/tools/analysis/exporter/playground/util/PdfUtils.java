package org.reactome.server.tools.analysis.exporter.playground.util;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.reactome.server.graph.domain.model.InstanceEdit;
import org.reactome.server.graph.domain.model.Person;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;
import org.reactome.server.tools.analysis.exporter.playground.model.IdentifierFound;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);
    private static final String LINKICON = "src/main/resources/images/link.png";
    //    private static final String LINKICON = "https://image.flaticon.com/icons/svg/684/684816.svg";
    public static Map<Float, Image> icons = new HashMap<>();
    public static long linkIconScaleTime;
    public static long indentifierFilteredTime;


    public static Image createImage(BufferedImage bufferedImage) throws Exception {
        return new Image(ImageDataFactory.create(bufferedImage, java.awt.Color.WHITE));
    }

    public static Image createImage(String fileName) throws FailToAddLogoException {
        try {
            return new Image(ImageDataFactory.create(fileName));
        } catch (MalformedURLException e) {
            throw new FailToAddLogoException("Failed to add image in pdf file", e);
        }
    }

    public static Image createGoToLinkIcon(Image icon, float width, String destination) throws NullLinkIconDestinationException {
        if (destination == null)
            throw new NullLinkIconDestinationException("Link icon's destination should not be null!");
        return ImageAutoScale(icon, width).setAction(PdfAction.createGoTo(destination));
    }

    public static Image createUrlLinkIcon(Image icon, float width, String url) throws NullLinkIconDestinationException {
        if (url == null) throw new NullLinkIconDestinationException("Link icon's url should not be null!");
        return ImageAutoScale(icon, width).setAction(PdfAction.createURI(url));
    }

    /**
     * scale image's size to fit the analysis report's page size.
     *
     * @param image
     * @param width aim width you want to reach.
     * @return
     */
//in this way it will spent about 10ms
//    public static Image ImageAutoScale(Image image, float width) {
//        long start = Instant.now().toEpochMilli();
//        width *= 0.75;//the icon's size will be slightly smaller than the font's size
//        if (icons.containsKey(width)) {
//            return icons.get(width);
//        } else {
//            float scaling = image.getImageWidth() >= width ? width / image.getImageWidth() : image.getImageWidth() / width;
//            image = image.scale(scaling, scaling);
//            icons.put(width, image);
//        }
//        linkIconScaleTime += (Instant.now().toEpochMilli() - start);
//        return image;
//    }
    //in this way it will spent total about 11ms.
    public static Image ImageAutoScale(Image image, float width) {
        long start = Instant.now().toEpochMilli();
        width *= 0.75;//the icon's size will be slightly smaller than the font's size
        float scaling = image.getImageWidth() >= width ? width / image.getImageWidth() : image.getImageWidth() / width;
        image = image.scale(scaling, scaling);
        linkIconScaleTime += (Instant.now().toEpochMilli() - start);
        return image;
    }

    public static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    /**
     * concat all stId into a long String as the http post's parameter entities.
     *
     * @param pathways all pathways need to show.
     * @return
     */
    public static StringBuilder stIdConcat(List<Pathway> pathways) {
        StringBuilder stringBuilder = new StringBuilder(15 * pathways.size());
        pathways.forEach(pathway -> stringBuilder.append(pathway.getStId()).append(','));
        return stringBuilder;
    }

    /**
     * merge the identifiers from all different pathways into a unique array.
     *
     * @param identifierFounds
     * @return
     */
//    @Monitor(name = "filter identifier")
    public static Map<String, Identifier> identifiersFilter(List<IdentifierFound> identifierFounds) {
        /**
         * in general there just about 1/4 of identifiers was unique(since there have a lot of redundancies)
         */
        long start = Instant.now().toEpochMilli();
        Map<String, Identifier> filteredIdentifiers = new HashMap<>((int) (identifierFounds.size() * 0.25));
        identifierFounds.forEach(identifierFound ->
                identifierFound.getEntities().forEach(identifier ->
                        {
                            if (!filteredIdentifiers.containsKey(identifier.getId())) {
                                filteredIdentifiers.put(identifier.getId(), identifier);
                            } else {
                                filteredIdentifiers.get(identifier.getId()).getMapsTo().addAll(identifier.getMapsTo());
                            }
                        }
                ));

        filteredIdentifiers.forEach((k, identifier) -> identifier.getMapsTo().forEach(mapsTo -> {
            if (!identifier.getResourceMapsToIds().containsKey(mapsTo.getResource())) {
                identifier.getResourceMapsToIds().put(mapsTo.getResource(), mapsTo.getIds().toString());
            } else {
                identifier.getResourceMapsToIds().get(mapsTo.getResource()).concat(',' + mapsTo.getIds().toString());
            }
        }));
        indentifierFilteredTime += (Instant.now().toEpochMilli() - start);
        return filteredIdentifiers;
    }

    @Monitor(name = "getdata")
    public static DataSet getDataSet(ReportArgs reportArgs, int pathwaysToShow) throws Exception {
        DataSet dataSet = new DataSet(reportArgs);
        dataSet.setLinkIcon(createImage(LINKICON));
        dataSet.setDBVersion(GraphCoreHelper.getDBVersion());


//        long start = Instant.now().toEpochMilli();
//        AnalysisResult analysisResult = HttpClientHelper.getResultAssociatedWithToken(URL.RESULTASSCIATEDWITHTOKEN, reportArgs.getToken());
//        dataSet.setIdentifiersWasNotFounds(HttpClientHelper.getIdentifiersWasNotFound(URL.IDENTIFIERSWASNOTFOUND, reportArgs.getToken()));
//
//        StringBuilder stIds = PdfUtils.stIdConcat(analysisResult.getPathways());
//        dataSet.setIdentifierFounds(HttpClientHelper.getIdentifierWasFound(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), reportArgs.getToken()));
//        LOGGER.info("spent {}ms to request resources from analysis service", Instant.now().toEpochMilli() - start);

        HttpClientHelper.fillDataSet(reportArgs, dataSet);
//
        dataSet.setIdentifiersWasFiltered(PdfUtils.identifiersFilter(dataSet.getIdentifierFounds()));

        //reduce the size of pathway array to save memory.
//        analysisResult.setPathways(analysisResult.getPathways().stream().limit(pathwaysToShow).collect(Collectors.toList()));
//        dataSet.setAnalysisResult(analysisResult);
        dataSet.setPathwaysToShow(pathwaysToShow);
        return dataSet;
    }

    public static PdfFont createFont(String fontName) throws IOException {
        return PdfFontFactory.createFont(fontName, "", false, false);
    }

    public static Color createColor(String color, int radix) {
        return new DeviceRgb(Integer.valueOf(color.substring(1, 3), radix)
                , Integer.valueOf(color.substring(3, 5), radix)
                , Integer.valueOf(color.substring(5, 7), radix));
    }


    public static String getInstanceEditName(InstanceEdit instanceEdit) {
        List<Person> authors = instanceEdit.getAuthor();
        StringBuilder name = new StringBuilder();
        authors.forEach(person -> name.append(person.getSurname())
                .append(' ')
                .append(person.getFirstname()).append(',')
                .append(instanceEdit.getDateTime().substring(0, 10))
                .append("\r\n"));
        return name.toString();
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
