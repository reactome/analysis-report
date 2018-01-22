package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToAddLogoException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.model.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String LINKICON = "src/main/resources/images/link.png";

    public static Image createImage(BufferedImage bufferedImage) throws Exception {
        return new Image(ImageDataFactory.create(bufferedImage, java.awt.Color.WHITE));
    }

    public static Image createImage(String fileName) throws FailToAddLogoException {
        try {
            return new Image(ImageDataFactory.create(fileName));
        } catch (MalformedURLException e) {
            throw new FailToAddLogoException("Failed to add logo in pdf file", e);
        }
    }

    public static Image createGoToLinkIcon(float width, String destination) throws FailToAddLogoException, NullLinkIconDestinationException {
        if (destination == null)
            throw new NullLinkIconDestinationException("Link icon's destination should not be null!");
        return ImageAutoScale(createImage(LINKICON), width).setAction(PdfAction.createGoTo(destination));
    }

    public static Image createUrlLinkIcon(float width, String url) throws FailToAddLogoException, NullLinkIconDestinationException {
        if (url == null) throw new NullLinkIconDestinationException("Link icon's url should not be null!");
        return ImageAutoScale(createImage(LINKICON), width).setAction(PdfAction.createURI(url));
    }

    /**
     * scale image's size to fit the analysis report's page size.
     *
     * @param image
     * @param width aim width you want to reach.
     * @return
     */
    public static Image ImageAutoScale(Image image, float width) {
        width *= 0.75;//the icon's size will be smaller than the font's size
//        float pageWidth = report.getPdfDocument().getDefaultPageSize().getWidth() - report.getLeftMargin() - report.getRightMargin();
        float scaling = image.getImageWidth() >= width ? width / image.getImageWidth() : image.getImageWidth() / width;
        return image.scale(scaling, scaling);
    }

    public static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }


    public static Paragraph setDestination(Paragraph paragraph, String destination) {
        paragraph.setProperty(Property.DESTINATION, destination);
        return paragraph;
    }

    /**
     * concat all stId into a long String as the http post's parameter entities.
     *
     * @param pathways all pathways need to show.
     * @return
     */
    public static StringBuilder stIdConcat(Pathway[] pathways) {
        StringBuilder stringBuilder = new StringBuilder(15 * pathways.length);
        for (Pathway pathway : pathways) {
            stringBuilder.append(pathway.getStId()).append(',');
        }
        return stringBuilder;
    }

    /**
     * merge the identifiers from all different pathways into a unique array.
     *
     * @param identifiersWasFounds
     * @return
     */
    // TODO: 09/01/18 this method need to optimize
    public static Map<String, Identifier> identifiersFilter(IdentifiersWasFound[] identifiersWasFounds) {
        /**
         * in general there just about 1/4 of identifiers was unique(since there have a lot of redundancies)
         */
        Map<String, Identifier> filteredIdentifiers = new HashMap<>((int) (identifiersWasFounds.length * 0.25));
        for (IdentifiersWasFound identifiersWasFound : identifiersWasFounds) {
            for (Identifier identifier : identifiersWasFound.getEntities()) {
                if (!filteredIdentifiers.containsKey(identifier.getId())) {
                    filteredIdentifiers.put(identifier.getId(), identifier);
                } else {
                    filteredIdentifiers.get(identifier.getId()).getMapsTo().addAll(identifier.getMapsTo());
                }
            }
        }

        for (Identifier identifier : filteredIdentifiers.values()) {
            for (MapsTo mapsTo : identifier.getMapsTo()) {
                if (!identifier.getResourceMapsToIds().containsKey(mapsTo.getResource())) {
                    identifier.getResourceMapsToIds().put(mapsTo.getResource(), mapsTo.getIds().toString());
                } else {
                    identifier.getResourceMapsToIds().get(mapsTo.getResource()).concat(',' + mapsTo.getIds().toString());
                }
            }
        }
        return filteredIdentifiers;
    }

    // TODO: 06/12/17 this method may be deleted once the correct dataset structure was confirm
    public static DataSet getDataSet(String token, int numOfPathwaysToShow) throws Exception {
        DataSet dataSet = new DataSet();
        ResultAssociatedWithToken resultAssociatedWithToken = HttpClientHelper.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, token);
        dataSet.setIdentifiersWasNotFounds(HttpClientHelper.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, token));

        StringBuilder stIds = PdfUtils.stIdConcat(resultAssociatedWithToken.getPathways());
        IdentifiersWasFound[] identifiersWasFounds = HttpClientHelper.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, token);
        dataSet.setIdentifiersWasFounds(identifiersWasFounds);
        dataSet.setIdentifiersWasFiltered(PdfUtils.identifiersFilter(identifiersWasFounds));

        //reduce the size of pathway array to save memory.
        resultAssociatedWithToken.setPathways(Arrays.copyOf(resultAssociatedWithToken.getPathways(), numOfPathwaysToShow));
        dataSet.setResultAssociatedWithToken(resultAssociatedWithToken);
        dataSet.setNumOfPathwaysToShow(numOfPathwaysToShow);
        dataSet.setVersion(HttpClientHelper.getForObject(URL.VERSION, Integer.class, ""));
        return dataSet;
    }

//    public _DataSet getDataSet(String token) throws Exception {
//        return MAPPER.readValue(CLIENT.execute(GET).getEntity().getContent(),_DataSet.class);
//    }

    public static PdfFont createFont(String fontName) throws IOException {
        return PdfFontFactory.createFont(fontName);
    }

    public static Color createColor(String color) {
        return new DeviceRgb(Integer.valueOf(color.substring(1, 3), 16)
                , Integer.valueOf(color.substring(3, 5), 16)
                , Integer.valueOf(color.substring(5, 7), 16));
    }

    public static <T> T readValue(File src, Class<T> type) throws IOException {
        return MAPPER.readValue(src, type);
    }

    public static <T> T readValue(InputStream src, Class<T> type) throws IOException {
        return MAPPER.readValue(src, type);
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

//    public _DataSet getDataSet() {
//        // TODO: 21/12/17 connect server by httpurlconnection
//        return new _DataSet();
//    }

}
