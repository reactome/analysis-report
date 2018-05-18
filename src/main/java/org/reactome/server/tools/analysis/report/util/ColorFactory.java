package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.tools.diagram.exporter.raster.profiles.GradientSheet;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ColorFactory {
	private static final Properties COLORS = new Properties();
	private static final Pattern RGBA = Pattern.compile("rgba\\((.*)\\)");
	private static final Pattern RGB = Pattern.compile("rgb\\((.*)\\)");
	private static final Map<String, Color> cache = new HashMap<>();
	private static final float INV_255 = 0.003921569F;

	static {
		try {
			COLORS.load(ColorFactory.class.getResourceAsStream("colors"));
		} catch (IOException e) {
			LoggerFactory.getLogger(ColorFactory.class).error("Couldn't load resource colors");
		}
	}

	public ColorFactory() {
	}

	public static Color parseColor(String color) {
		return color != null && !color.trim().isEmpty() ? cache.computeIfAbsent(color, ColorFactory::strToColor) : null;
	}

	private static Color strToColor(String color) {
		color = COLORS.getProperty(color, color); // Check if code is in properties
		return color.startsWith("#") ? hexToColor(color) : rgbaToColor(color);
	}

	private static Color hexToColor(String input) {
		int r = Integer.valueOf(input.substring(1, 3), 16);
		int g = Integer.valueOf(input.substring(3, 5), 16);
		int b = Integer.valueOf(input.substring(5, 7), 16);
		return new Color(r, g, b);
	}

	private static Color rgbaToColor(String input) {
		Matcher m = RGB.matcher(input);
		if (m.matches()) {
			String[] rgba = m.group(1).split(",");
			return new Color(Integer.parseInt(rgba[0].trim()), Integer.parseInt(rgba[1].trim()), Integer.parseInt(rgba[2].trim()));
		}
		m = RGBA.matcher(input);
		if (m.matches()) {
			String[] rgba = m.group(1).split(",");
			return new Color(Integer.parseInt(rgba[0].trim()), Integer.parseInt(rgba[1].trim()), Integer.parseInt(rgba[2].trim()), (int) (255.0F * Float.parseFloat(rgba[3].trim())));
		}
		return null;
	}

	public static Color blend(Color back, Color front) {
		double b = (double) back.getAlpha() / 255.0D;
		double f = (double) front.getAlpha() / 255.0D;
		double alpha = b * f + (1.0D - b);
		int red = (int) ((double) back.getRed() * b + (double) front.getRed() * f);
		int green = (int) ((double) back.getGreen() * b + (double) front.getGreen() * f);
		int blue = (int) ((double) back.getBlue() * b + (double) front.getBlue() * f);
		return new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue), Math.min(255, (int) (alpha * 255.0D)));
	}

	public static Color interpolate(GradientSheet gradient, double scale) {
		if (gradient.getStop() == null) {
			return interpolate(gradient.getMin(), gradient.getMax(), scale);
		} else {
			return scale < 0.5D ? interpolate(gradient.getMin(), gradient.getStop(), scale * 2.0D) : interpolate(gradient.getStop(), gradient.getMax(), (scale - 0.5D) * 2.0D);
		}
	}

	private static Color interpolate(Color a, Color b, double t) {
		if (t <= 0.0D) {
			return a;
		} else if (t >= 1.0D) {
			return b;
		} else {
			float scale = (float) t;
			return new Color((int) ((float) a.getRed() + (float) (b.getRed() - a.getRed()) * scale), (int) ((float) a.getGreen() + (float) (b.getGreen() - a.getGreen()) * scale), (int) ((float) a.getBlue() + (float) (b.getBlue() - a.getBlue()) * scale), (int) ((float) a.getAlpha() + (float) (b.getAlpha() - a.getAlpha()) * scale));
		}
	}

	public static String hex(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	public static String rgba(Color color) {
		float alpha = (float) color.getAlpha() * 0.003921569F;
		String a;
		if ((double) alpha > 0.99D) {
			a = "1";
		} else {
			a = String.format("%.2f", alpha);
		}

		return String.format(Locale.UK, "rgba(%d,%d,%d,%s)", color.getRed(), color.getGreen(), color.getBlue(), a);
	}

	public static String getColorMatrix(Color color) {
		float r = (float) color.getRed() * 0.003921569F;
		float g = (float) color.getGreen() * 0.003921569F;
		float b = (float) color.getBlue() * 0.003921569F;
		float a = (float) color.getAlpha() * 0.003921569F;
		Float[] floats = new Float[]{0.0F, 0.0F, 0.0F, r, 0.0F, 0.0F, 0.0F, 0.0F, g, 0.0F, 0.0F, 0.0F, 0.0F, b, 0.0F, 0.0F, 0.0F, 0.0F, a, 0.0F};
		java.util.List<String> strings = Arrays.stream(floats).map(String::valueOf).collect(Collectors.toList());
		return String.join(" ", strings);
	}

}
