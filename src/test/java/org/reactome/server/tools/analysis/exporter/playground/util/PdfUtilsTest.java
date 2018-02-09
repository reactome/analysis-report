package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtilsTest {

    @Test
    public void getText() {
        String[] LITERATURE = PdfUtils.getText("src/main/resources/texts/references.txt");
        for (String string : LITERATURE) {
            System.out.println(string);
            System.out.println(string.split("\t").length);
        }
        HashMap<String, StringBuilder> strings = new HashMap<>();
        strings.put("123", new StringBuilder("123"));
        System.out.println(strings.get("123"));
        strings.get("123").append("qwe");
        Identifier identifier01 = new Identifier();
        identifier01.setId("123qwe");
        Identifier identifier02 = new Identifier();
        identifier02.setId("123qw");
        Set<Identifier> identifiers = new HashSet<>();
        System.out.println(identifiers.add(identifier01));
        System.out.println(identifiers.add(identifier02));
    }
}