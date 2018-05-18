package org.reactome.server.tools.analysis.report;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import com.steadystate.css.parser.selectors.ClassConditionImpl;
import com.steadystate.css.parser.selectors.ConditionalSelectorImpl;
import org.slf4j.LoggerFactory;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StyleSheet {

	private CSSStyleSheet sheet;

	public StyleSheet(String style) {

		InputSource source = new InputSource(new StringReader(style));
		CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
		try {
			sheet = parser.parseStyleSheet(source, null, null);
		} catch (IOException e) {
			LoggerFactory.getLogger(StyleSheet.class).error("Couldn't parser style", e);
		}
	}

	public Map<String, String> getStyle(String selector) {
		final Map<String, String> map = new LinkedHashMap<>();
		for (int i = 0; i < sheet.getCssRules().getLength(); i++) {
			final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) sheet.getCssRules().item(i);
			for (int j = 0; j < rule.getSelectors().getLength(); j++) {
				final ConditionalSelectorImpl sel = (ConditionalSelectorImpl) rule.getSelectors().item(j);
				final ClassConditionImpl condition = (ClassConditionImpl) sel.getCondition();
				final String possiblyAClass = condition.getValue();
				if (possiblyAClass.equals(selector)) {
					for (int k = 0; k < rule.getStyle().getLength(); k++) {
						final String key = rule.getStyle().item(k);
						final String value = rule.getStyle().getPropertyValue(key);
						map.put(key, value);
					}
				}
			}
		}
		return map;
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

	private Stream<Node> stream(NodeList list) {
		return IntStream.range(0, list.getLength())
				.mapToObj(list::item);
	}

}
