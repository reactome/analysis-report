package org.reactome.server.tools.analysis.exporter.constant;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;

/**
 * The basic {@link Color} used in report PDF document.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Colors {
	public static final Color REACTOME_COLOR = new DeviceRgb(47, 158, 194); //reactome website color: "#2F9EC2"
	public static final Color GRAY = new DeviceGray(.86f);
	public static final Color WHITE = DeviceRgb.WHITE;
}
