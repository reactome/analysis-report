package org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ListParagraph extends P {

    public ListParagraph(String text) {
        super(text);
        setMultipliedLeading(1.0f);
    }
}