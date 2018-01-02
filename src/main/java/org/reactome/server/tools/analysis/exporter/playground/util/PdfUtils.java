package org.reactome.server.tools.analysis.exporter.playground.util;

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
public abstract class PdfUtils {

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
        StringBuilder stringBuilder = new StringBuilder();
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
                    entry.getValue().getResourceMapsToIds().get(mapsTo.getResource()).concat("," + mapsTo.getIds().toString());
                }
            }
        }
        return filteredIdentifiers;
    }

    // TODO: 06/12/17 this method may be deleted once the correct dataset structure was confirm
    public static DataSet getDataSet(String token) throws Exception {
        DataSet dataSet = new DataSet();
        ResultAssociatedWithToken resultAssociatedWithToken = HttpClientHelper.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, token);
        Pathway[] pathways = resultAssociatedWithToken.getPathways();
        StringBuilder stIds = PdfUtils.stIdConcat(pathways);
        IdentifiersWasFound[] identifiersWasFounds = HttpClientHelper.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, token);
        Map<String, Identifier> identifiersWasFiltered = PdfUtils.identifiersFilter(identifiersWasFounds);
        Identifier[] identifiersWasNotFounds = HttpClientHelper.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, token);
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

    public _DataSet getDataSet() {
        // TODO: 21/12/17 connect server by httpurlconnection
        return new _DataSet();
    }



}
