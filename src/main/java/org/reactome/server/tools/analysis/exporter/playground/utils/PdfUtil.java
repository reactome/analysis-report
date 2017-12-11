package org.reactome.server.tools.analysis.exporter.playground.utils;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.models.*;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfUtil {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm dd/MM/yyyy");
//    private static final ObjectMapper MAPPER = new ObjectMapper();
//    private static final CloseableHttpClient CLIENT = HttpClients.createDefault();
//    private static final HttpGet GET = new HttpGet("final data set url");

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
        return SIMPLE_DATE_FORMAT.format(new Date());
    }

    public static Paragraph setDestination(Paragraph paragraph, String destination) {
        paragraph.setProperty(Property.DESTINATION, destination);
        return paragraph;
    }

    public static StringBuilder stIdConcat(Pathway[] pathways) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pathway pathway : pathways)
            stringBuilder.append(pathway.getStId()).append(',');
        return stringBuilder;
    }

    public static Map<String, Identifier> identifiersFilter(IdentifiersWasFound[] identifiersWasFounds) {
        Map<String, Identifier> filteredIdentifiers = new HashMap<>();
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

    // TODO: 06/12/17 this method need may be deleted because of the correct dataset structure was confirm
    public static DataSet getDataSet(PdfProperties properties) throws Exception{
        DataSet dataSet = new DataSet();
        ResultAssociatedWithToken resultAssociatedWithToken =  HttpClientHelper.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, properties.getToken());
        Pathway[] pathways = resultAssociatedWithToken.getPathways();
        StringBuilder stIds = PdfUtil.stIdConcat(pathways);
        IdentifiersWasFound[] identifiersWasFounds = HttpClientHelper.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, properties.getToken());
        Map<String, Identifier> identifiersWasFiltered = PdfUtil.identifiersFilter(identifiersWasFounds);
        Identifier[] identifiersWasNotFounds = HttpClientHelper.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, properties.getToken());
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

}
