package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.graph.domain.model.InstanceEdit;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Person;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Takes every edit in a pathway, collects them by date and action type, and
 * generates a list sorted by date.
 */
public class Editions extends LinkedList<Editions.Edition> {

	private Editions() {
	}

	public static List<Edition> from(Pathway pathway) {
		final Map<String, Map<String, Set<Person>>> edits = new LinkedHashMap<>();
		add(edits, "Created", pathway.getCreated());
		add(edits, "Modified", pathway.getModified());
		add(edits, "Authored", pathway.getAuthored());
		add(edits, "Edited", pathway.getEdited());
		add(edits, "Reviewed", pathway.getReviewed());
		add(edits, "Revised", pathway.getRevised());
		final LinkedList<Edition> editions = new LinkedList<>();
		edits.forEach((date, map) -> map.forEach((action, people) -> {
			editions.add(new Edition(date, action, people));
		}));
		editions.sort(Comparator.comparing(Edition::getDate));
		return editions;
	}

	private static void add(Map<String, Map<String, Set<Person>>> edits, String type, InstanceEdit edit) {
		if (edit != null)
			edits.computeIfAbsent(edit.getDateTime().substring(0, 10), k -> new LinkedHashMap<>())
					.computeIfAbsent(type, k -> new TreeSet<>())
					.addAll(edit.getAuthor());
	}

	private static void add(Map<String, Map<String, Set<Person>>> edits, String type, List<InstanceEdit> instanceEdits) {
		if (instanceEdits != null)
			instanceEdits.forEach(edit -> add(edits, type, edit));
	}

	private static String asString(Collection<Person> persons) {
		if (persons == null) return "";
		String text = String.join(", ", persons.stream()
				.limit(5)
				.map(Editions::compileName)
				.collect(Collectors.toList()));
		if (persons.size() > 5) text += " et al.";
		return text;
	}

	private static String compileName(Person person) {
		if (person.getFirstname() != null && person.getSurname() != null)
			return person.getSurname() + " " + person.getFirstname();
		if (person.getSurname() != null)
			return person.getSurname();
		return person.getFirstname();
	}


	public static class Edition {
		private final String date;
		private final String action;
		private final String people;

		Edition(String date, String action, Set<Person> people) {
			this.date = date;
			this.action = action;
			this.people = asString(people);
		}

		public String getDate() {
			return date;
		}

		public String getType() {
			return action;
		}

		public String getAuthors() {
			return people;
		}
	}
}
