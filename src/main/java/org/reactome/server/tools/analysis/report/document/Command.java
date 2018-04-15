package org.reactome.server.tools.analysis.report.document;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Command {

	private final String name;
	private String modifiers;
	private List<String> values;
	private String floats;

	public Command(String name) {
		this.name = name;
	}

	public Command(String name, String... values) {
		this.name = name;
		if (values != null)
			this.values = Arrays.asList(values);
	}

	public Command floats(String floats) {
		this.floats = floats;
		return this;
	}

	public Command modifiers(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}

	public Command values(String... values) {
		if (values != null) this.values = Arrays.asList(values);
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder line = new StringBuilder("\\").append(name);
		if (modifiers != null)
			line.append("[").append(modifiers).append("]");
		if (values != null)
			values.stream().filter(Objects::nonNull).forEach(value -> line.append("{").append(value).append("}"));
		if (floats != null)
			line.append("[").append(floats).append("]");
		return line.toString();
	}
}
