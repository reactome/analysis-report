package org.reactome.server.tools.analysis.report.document;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Command {

	private final String name;
	private String modifiers;
	private List<String> values;
	private String floats;

	public Command(String name) {
		this.name = name;
	}

	public Command(String name, Object... values) {
		this.name = name;
		values(values);
	}

	public Command floats(String floats) {
		this.floats = floats;
		return this;
	}

	public Command modifiers(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}

	public Command values(Object... values) {
		if (values != null)
			this.values = Arrays.stream(values)
					.filter(Objects::nonNull)
					.map(String::valueOf)
					.collect(Collectors.toList());
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder line = new StringBuilder("\\").append(name);
		if (modifiers != null)
			line.append("[").append(modifiers).append("]");
		if (values != null && !values.isEmpty())
			values.forEach(value -> line.append("{").append(value).append("}"));
		if (floats != null)
			line.append("[").append(floats).append("]");
		return line.toString();
	}
}
