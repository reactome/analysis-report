package org.reactome.server.tools.analysis.exporter.style;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;

/**
 * Contains the constant value of font size. <p>just set the font size of
 * paragraph and all anther font size will been set relative to that.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Profile {

	public static final float TITLE = 24;
	public static final float H1 = 20;
	public static final float H2 = 16;
	public static final float H3 = 14;
	public static final float H4 = 12;
	public static final float P = 10;
	public static final float TABLE = 8;

	public static final Color REACTOME_COLOR = new DeviceRgb(47, 158, 194);
	public static final Color LIGHT_GRAY = new DeviceGray(0.9f);
	public static final Color LINK_COLOR = REACTOME_COLOR;


}
