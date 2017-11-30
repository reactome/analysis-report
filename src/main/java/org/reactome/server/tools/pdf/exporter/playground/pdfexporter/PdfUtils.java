package org.reactome.server.tools.pdf.exporter.playground.pdfexporter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import org.reactome.server.tools.pdf.exporter.playground.constants.URL;
import org.reactome.server.tools.pdf.exporter.playground.domains.*;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.pdf.exporter.playground.resttemplate.RestTemplateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@Component
public class PdfUtils {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm dd/MM/yyyy");

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


    public static void addLogo(Document document, String LOGO) throws MalformedURLException {
        Image logo = new Image(ImageDataFactory.create(LOGO));
        logo.scale(0.3f, 0.3f);
        logo.setFixedPosition(document.getLeftMargin() * 0.3f, document.getPdfDocument().getDefaultPageSize().getHeight() - document.getTopMargin() * 0.3f - logo.getImageScaledHeight());
        document.add(logo);
    }


    public static String getCreatedTime() {
        return SIMPLE_DATE_FORMAT.format(new Date());
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
        for (Map.Entry<String, Identifier> entry : filteredIdentifiers.entrySet()) {
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

    public static DataSet getDataSet(PdfProperties properties) {
        DataSet dataSet = new DataSet();
        RestTemplate restTemplate = RestTemplateFactory.getInstance();
        ResultAssociatedWithToken resultAssociatedWithToken = restTemplate.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, properties.getToken(), properties.getNumberOfPathwaysToShow());
        Pathway[] pathways = resultAssociatedWithToken.getPathways();
        StringBuilder stIds = PdfUtils.stIdConcat(pathways);
        IdentifiersWasFound[] identifiersWasFounds = restTemplate.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, properties.getToken());
        Map<String, Identifier> identifiersWasFiltered = PdfUtils.identifiersFilter(identifiersWasFounds);
        Identifier[] identifiersWasNotFounds = restTemplate.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, properties.getToken());
        dataSet.setIdentifiersWasNotFounds(identifiersWasNotFounds);
        dataSet.setIdentifiersWasFounds(identifiersWasFounds);
        dataSet.setResultAssociatedWithToken(resultAssociatedWithToken);
        dataSet.setToken(properties.getToken());
        dataSet.setIdentifiersWasFiltered(identifiersWasFiltered);
        dataSet.setPathways(pathways);
        return dataSet;
    }

}
