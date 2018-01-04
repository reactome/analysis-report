package org.reactome.server.tools.analysis.exporter.playground.util;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.model.*;
import org.reactome.server.tools.analysis.exporter.playground.model._._DataSet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfUtils {

    public static Image ImageAutoScale(Document document, Image image) {
        float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin();
        float stride = 0.01f;
        float scaling = 0.2f;
        image.scale(scaling, scaling);
        while (pageWidth < image.getImageScaledWidth()) {
            scaling -= stride;
            image.scale(scaling, scaling);
        }
        return image;
    }

    public static String getCreatedTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }


    public static Paragraph setDestination(Paragraph paragraph, String destination) {
        paragraph.setProperty(Property.DESTINATION, destination);
        return paragraph;
    }

    public static StringBuilder stIdConcat(Pathway[] pathways) {
        StringBuilder stringBuilder = new StringBuilder(2 * pathways.length + 1);
        Stream.of(pathways).forEach(pathway -> stringBuilder.append(pathway.getStId()).append(','));
        return stringBuilder;
    }

    // to merge the different identifiers to a unique array
    public static Map<String, Identifier> identifiersFilter(IdentifiersWasFound[] identifiersWasFounds) {
        // initial capacity of hash map is about:identifiersWasFounds * 1.33 + 1
        Map<String, Identifier> filteredIdentifiers = new HashMap<>((int) (identifiersWasFounds.length / 0.75) + 1);
        for (IdentifiersWasFound identifiersWasFound : identifiersWasFounds) {
            for (Identifier identifier : identifiersWasFound.getEntities()) {
                if (!filteredIdentifiers.containsKey(identifier.getId())) {
                    filteredIdentifiers.put(identifier.getId(), identifier);
                } else {
                    filteredIdentifiers.get(identifier.getId()).getMapsTo().addAll(identifier.getMapsTo());
                }
            }
        }

        for (Entry<String, Identifier> entry : filteredIdentifiers.entrySet()) {
            for (MapsTo mapsTo : entry.getValue().getMapsTo()) {
                if (!entry.getValue().getResourceMapsToIds().containsKey(mapsTo.getResource())) {
                    entry.getValue().getResourceMapsToIds().put(mapsTo.getResource(), mapsTo.getIds().toString());
                } else {
                    entry.getValue().getResourceMapsToIds().get(mapsTo.getResource()).concat(',' + mapsTo.getIds().toString());
                }
            }
        }

        return filteredIdentifiers;
    }

    // TODO: 06/12/17 this method may be deleted once the correct dataset structure was confirm
    public static DataSet getDataSet(String token) throws Exception {
        DataSet dataSet = new DataSet();
        ResultAssociatedWithToken resultAssociatedWithToken = HttpClientHelper.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, token);
        Identifier[] identifiersWasNotFounds = HttpClientHelper.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, token);
        Pathway[] pathways = resultAssociatedWithToken.getPathways();
        StringBuilder stIds = PdfUtils.stIdConcat(pathways);
        IdentifiersWasFound[] identifiersWasFounds = HttpClientHelper.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, token);
        Map<String, Identifier> identifiersWasFiltered = PdfUtils.identifiersFilter(identifiersWasFounds);
        dataSet.setIdentifiersWasNotFounds(identifiersWasNotFounds);
        dataSet.setIdentifiersWasFounds(identifiersWasFounds);
        dataSet.setResultAssociatedWithToken(resultAssociatedWithToken);
        dataSet.setIdentifiersWasFiltered(identifiersWasFiltered);
        dataSet.setPathways(pathways);
        return dataSet;
    }

//    public _DataSet getDataSet(String token) throws Exception {
//        return MAPPER.readValue(CLIENT.execute(GET).getEntity().getContent(),_DataSet.class);
//    }

    public static PageSize createPageSize(String type) throws Exception {
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
                throw new NoSuchFieldException(String.format("Cant recognize page size:%s", type));
        }
    }

    public _DataSet getDataSet() {
        // TODO: 21/12/17 connect server by httpurlconnection
        return new _DataSet();
    }

}
