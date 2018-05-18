package org.reactome.server.tools.analysis.report;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TransparentColor;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.junit.Test;
import org.reactome.server.tools.analysis.report.util.ColorFactory;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SvgToPdfTest {

	private static final SAXSVGDocumentFactory DOCUMENT_FACTORY = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

	@Test
	public void test() throws IOException {
//		final File file = new File("/media/pascual/Disco1TB/reactome/ehld/R-HSA-190236.svg");
		final File file = new File("src/test/resources/org/reactome/R-HSA-376176.svg");
		final SVGDocument svgDocument = DOCUMENT_FACTORY.createSVGDocument(file.getAbsolutePath());
		final PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File("test-svg.pdf")));
		final Document document = new Document(pdfDocument);
		document.setMargins(10, 10, 10, 10);
		final PdfPage page = pdfDocument.addNewPage(PageSize.A4);
		final PdfCanvas canvas = new PdfCanvas(page);
		final NodeList styleNodeList = svgDocument.getElementsByTagName("style");
		final StyleSheet styleSheet;
		if (styleNodeList.getLength() > 0) {
			final String style = styleNodeList.item(0).getTextContent();
			styleSheet = new StyleSheet(style);
		} else styleSheet = new StyleSheet("");
		transform(svgDocument, document, page, canvas);

//		canvas.rectangle(0, 0, 100, 100);
//		canvas.fill();
		render(canvas, styleSheet, Collections.emptyMap(), svgDocument.getRootElement());
		canvas.release();
		pdfDocument.close();
	}

	private void transform(SVGDocument svgDocument, Document document, PdfPage page, PdfCanvas canvas) {
		final float width = page.getPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin();
		final float height = page.getPageSize().getHeight() - document.getTopMargin() - document.getBottomMargin();

		final String viewBox = svgDocument.getRootElement().getAttribute("viewBox");
		final String[] split = viewBox.split(" ");
		final Double x = Double.valueOf(split[0]);
		final Double y = Double.valueOf(split[1]);
		final Double ow = Double.valueOf(split[2]);
		final Double oh = Double.valueOf(split[3]);

		final double factor = Math.min(width / ow, height / oh);
		final double xoff = document.getLeftMargin() + x * factor;
		final double yoff = height - document.getTopMargin() + y * factor;
		canvas.concatMatrix(factor, 0, 0, -factor, xoff, yoff);
	}

	private void render(PdfCanvas canvas, StyleSheet styleSheet, Map<String, String> style, Node node) {
		// Compute attributes
		final Map<String, String> subStyle = new LinkedHashMap<>(style);
		subStyle.putAll(extractAttributes(node));
		// Compute style from class
		if (subStyle.containsKey("class"))
			for (String cls : subStyle.get("class").split(" "))
				style.putAll(styleSheet.getStyle(cls));
		switch (node.getNodeName().toLowerCase()) {
			case "rect":
				renderRect(canvas, subStyle, node);
				break;
			case "circle":
				renderCircle(canvas, subStyle, node);
				break;
			case "ellipse":
				renderEllipse(canvas, subStyle, node);
				break;
			case "line":
				renderLine(canvas, subStyle, node);
				break;
			case "polygon":
				renderPolygon(canvas, subStyle, node);
				break;
			case "polyline":
				renderPolyline(canvas, subStyle, node);
				break;
			case "path":
				renderPath(canvas, subStyle, node);
				break;
		}
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
			render(canvas, styleSheet, subStyle, childNodes.item(i));
	}

	private void renderPath(PdfCanvas canvas, Map<String, String> style, Node node) {
		final String[] points = style.get("d").trim().split("\\s+");
		// H: horizontal line x+
		// V: vertical line y+
		int i = 0;
		String context = "";
		double controlX = 0;
		double controlY = 0;
		double x = 0;
		double y = 0;
		while (i < points.length) {
			final String point = points[i];
			if (point.startsWith("M")) {
				// M: move to (x y)+
				x = Double.valueOf(point.substring(1));
				y = Double.valueOf(points[++i]);
				canvas.moveTo(x, y);
				context = "L";
			} else if (point.startsWith("L")) {
				// L: line to (x y)+
				x = Double.valueOf(point.substring(1));
				y = Double.valueOf(points[++i]);
				canvas.lineTo(x, y);
				context = "L";
			} else if (point.startsWith("C")) {
				// C: curve to (x1 y1 x2 y2 x y)
				final Double x1 = Double.valueOf(point.substring(1));
				final Double y1 = Double.valueOf(points[++i]);
				final Double x2 = controlX = Double.valueOf(points[++i]);
				final Double y2 = controlY = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x2, y2, x, y);
				context = "C";
			} else if (point.startsWith("S")) {
				// S: curve to (x2 y2 x y)+
				final Double x1 = reflect(controlX, x);
				final Double y1 = reflect(controlY, y);
				final Double x2 = controlX = Double.valueOf(point.substring(1));
				final Double y2 = controlY = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x2, y2, x, y);
				context = "S";
			} else if (point.startsWith("ZM")) {
				// ZM: close n move to (x y)+
				canvas.closePath();
				stroke(canvas, style);
				fill(canvas, style);
				x = Double.valueOf(point.substring(2));
				y = Double.valueOf(points[++i]);
				canvas.moveTo(x, y);
				context = "L";
			} else if (point.startsWith("Q")) {
				// Q: curve to (x1 y1 x y)+
				final Double x1 = controlX = Double.valueOf(point.substring(1));
				final Double y1 = controlY = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x, y);
				context = "Q";
			} else if (point.startsWith("T")) {
				// T: curve to (x y)+
				final Double x1 = controlX = reflect(controlX, x);
				final Double y1 = controlY = reflect(controlY, y);
				x = Double.valueOf(point.substring(1));
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x, y);
				context = "T";
			}
			else if (point.startsWith("Z")) {
				// Z: close path
				canvas.closePath();
			} else if (context.equals("L")) {
				// line to (x y)+
				x = Double.valueOf(point);
				y = Double.valueOf(points[++i]);
				canvas.lineTo(x, y);
			} else if (context.equals("C")) {
				// curve to (x1 y1 x2 y2 x y)
				final Double x1 = Double.valueOf(point);
				final Double y1 = Double.valueOf(points[++i]);
				final Double x2 = Double.valueOf(points[++i]);
				final Double y2 = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x2, y2, x, y);
			} else if (context.equals("S")) {
				// curve to (x2 y2 x y)+
				final Double x2 = Double.valueOf(point);
				final Double y2 = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x2, y2, x, y);
			} else if (context.equals("Q")) {
				// Q: curve to (x1 y1 x y)+
				final Double x1 = controlX = Double.valueOf(point);
				final Double y1 = controlY = Double.valueOf(points[++i]);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x, y);
				context = "Q";
			} else if (context.startsWith("T")) {
				// T: curve to (x y)+
				final Double x1 = controlX = reflect(controlX, x);
				final Double y1 = controlY = reflect(controlY, y);
				x = Double.valueOf(points[++i]);
				y = Double.valueOf(points[++i]);
				canvas.curveTo(x1, y1, x, y);
				context = "T";
			}
			i++;
		}
		stroke(canvas, style);
		fill(canvas, style);

	}

	private Double reflect(double a, double pivot) {
		return pivot + pivot - a;
	}

	private void renderPolyline(PdfCanvas canvas, Map<String, String> style, Node node) {
		final String[] points = style.get("points").trim().split("\\s+");
		final Double x = Double.valueOf(points[0]);
		final Double y = Double.valueOf(points[1]);
		canvas.moveTo(x, y);
		for (int i = 2; i < points.length; i += 2) {
			final Double x1 = Double.valueOf(points[i]);
			final Double y1 = Double.valueOf(points[i + 1]);
			canvas.lineTo(x1, y1);
		}

		stroke(canvas, style);
	}

	private void renderPolygon(PdfCanvas canvas, Map<String, String> style, Node node) {
		final String[] points = style.get("points").trim().split("\\s+");
		final Double x = Double.valueOf(points[0]);
		final Double y = Double.valueOf(points[1]);
		canvas.moveTo(x, y);
		for (int i = 2; i < points.length; i += 2) {
			final Double x1 = Double.valueOf(points[i]);
			final Double y1 = Double.valueOf(points[i + 1]);
			canvas.lineTo(x1, y1);
		}
		canvas.closePath();

		stroke(canvas, style);
		fill(canvas, style);
	}

	private void renderLine(PdfCanvas canvas, Map<String, String> style, Node node) {
		final Double x1 = Double.valueOf(style.get("x1"));
		final Double x2 = Double.valueOf(style.get("x2"));
		final Double y1 = Double.valueOf(style.get("y1"));
		final Double y2 = Double.valueOf(style.get("y2"));
		canvas.moveTo(x1, y1);
		canvas.lineTo(x2, y2);
		stroke(canvas, style);
	}

	private void renderEllipse(PdfCanvas canvas, Map<String, String> style, Node node) {
		// ‘cx’
		//‘cy’
		//‘rx’
		//‘ry’
		final Double rx = Double.valueOf(style.get("rx"));
		final Double ry = Double.valueOf(style.get("ry"));
		final Double cx = Double.valueOf(style.get("cx"));
		final Double cy = Double.valueOf(style.get("cy"));
		canvas.ellipse(cx - rx, cy - ry, cx + rx, cy + ry);

		fill(canvas, style);
		stroke(canvas, style);
	}

	private Map<String, String> extractAttributes(Node node) {
		final Map<String, String> map = new LinkedHashMap<>();
		stream(node.getAttributes()).forEach(attr -> {
			final String key = attr.getNodeName();
			final String value = attr.getNodeValue();
			map.put(key, value);
		});
		return map;
	}

	private Stream<Node> stream(NamedNodeMap attributes) {
		return attributes == null
				? Stream.empty()
				: IntStream.range(0, attributes.getLength())
				.mapToObj(attributes::item);
	}

	private Stream<Node> stream(NodeList list) {
		return IntStream.range(0, list.getLength())
				.mapToObj(list::item);
	}

	private <T extends Selector> Stream<T> stream(SelectorList list, Class<T> tClass) {
		return IntStream.range(0, list.getLength())
				.mapToObj(list::item)
				.map(tClass::cast);
	}

	private <T extends CSSRule> Stream<T> stream(CSSRuleList list, Class<T> tClass) {
		return IntStream.range(0, list.getLength())
				.mapToObj(list::item)
				.map(tClass::cast);
	}

	private void renderCircle(PdfCanvas canvas, Map<String, String> style, Node node) {
		final Double r = Double.valueOf(style.get("r"));
		final Double cx = Double.valueOf(style.get("cx"));
		final Double cy = Double.valueOf(style.get("cy"));
		canvas.circle(cx, cy, r);
		fill(canvas, style);
		stroke(canvas, style);
	}

	private void renderRect(PdfCanvas canvas, Map<String, String> style, Node node) {
		final Double x = Double.valueOf(style.get("x"));
		final Double y = Double.valueOf(style.get("y"));
		final Double width = Double.valueOf(style.get("width"));
		final Double height = Double.valueOf(style.get("height"));
		Double rx = null;
		if (style.containsKey("rx"))
			rx = Double.valueOf(style.get("rx"));
		else if (style.containsKey("ry"))
			rx = Double.valueOf(style.get("ry"));
		if (rx != null)
			canvas.roundRectangle(x, y, width, height, rx);
		else canvas.rectangle(x, y, width, height);
		fill(canvas, style);
		stroke(canvas, style);
	}

	private void fill(PdfCanvas canvas, Map<String, String> style) {
		final Color fill = getFill(style);
		if (fill != null) {
//			canvas.setFillColor(new DeviceRgb(fill.getRed(), fill.getGreen(), fill.getBlue()));
			TransparentColor color = new TransparentColor(new DeviceRgb(fill.getRed(), fill.getGreen(), fill.getBlue()), fill.getAlpha() / 255.0f);
			canvas.setFillColor(color.getColor());
			color.applyFillTransparency(canvas);
			canvas.fill();
		}
	}

	private void stroke(PdfCanvas canvas, Map<String, String> style) {
		final Color stroke = getStroke(style);
		if (stroke != null) {
//			canvas.setStrokeColor(new DeviceRgb(stroke.getRed(), stroke.getGreen(), stroke.getBlue()));
			TransparentColor color = new TransparentColor(new DeviceRgb(stroke.getRed(), stroke.getGreen(), stroke.getBlue()), stroke.getAlpha() / 255.0f);
			canvas.setStrokeColor(color.getColor());
			color.applyStrokeTransparency(canvas);
			canvas.stroke();
		}
	}

	private Color getStroke(Map<String, String> style) {
		if (style.containsKey("stroke"))
			return paint(style.get("stroke"));
		else if (style.containsKey("color"))
			return paint(style.get("color"));
		else return null;
	}


	private Color getFill(Map<String, String> style) {
		if (style.containsKey("fill"))
			return paint(style.get("fill"));
		else if (style.containsKey("color"))
			return paint(style.get("color"));
		else return null;
	}

	private Color paint(String value) {
		if (value.equalsIgnoreCase("none"))
			return null;
		else return ColorFactory.parseColor(value);
	}
}
