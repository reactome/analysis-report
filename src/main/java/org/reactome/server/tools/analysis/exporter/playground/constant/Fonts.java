package org.reactome.server.tools.analysis.exporter.playground.constant;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * One font just only can be used in one specific PDF document{@see Document}.
 * so fonts need to be re-initialise before use it.
 *
 * @author Pascual/Chuan-Deng dengchuanbio@gmail.com
 */
public class Fonts {

    public static PdfFont LIGHT;
    public static PdfFont REGULAR;
    public static PdfFont BOLD;

    public static void setUp() {
        new Fonts().updateFonts();
    }

    private void updateFonts() {
        // Every PDF must load the fonts again, as they are hold by one, and only one document
        try {
            byte[] bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("fonts/Open_Sans/OpenSans-Regular.ttf"));
            REGULAR = PdfFontFactory.createFont(bytes, "");
            bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("fonts/Open_Sans/OpenSans-Bold.ttf"));
            BOLD = PdfFontFactory.createFont(bytes, "");
            bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("fonts/Open_Sans/OpenSans-Light.ttf"));
            LIGHT = PdfFontFactory.createFont(bytes, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
