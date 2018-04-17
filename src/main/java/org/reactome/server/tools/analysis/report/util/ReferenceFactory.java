package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.graph.domain.model.*;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReferenceFactory {


	public static String toString(Publication publication) {
		if (publication instanceof LiteratureReference) {
			return toString((LiteratureReference) publication);
		} else if (publication instanceof Book) {
			return toString((Book) publication);
		} else if (publication instanceof URL) {
			return toString((URL) publication);
		} else return publication.getDisplayName();
	}

	private static String toString(LiteratureReference reference) {
		return joinNonNullNonEmpty(", ",
				asString(reference.getAuthor()),
				reference.getTitle(),
				reference.getJournal(),
				reference.getVolume(),
				reference.getYear(),
				reference.getPages(),
				reference.getPubMedIdentifier());
	}

	private static String toString(Book book) {
		return joinNonNullNonEmpty(", ",
				asString(book.getAuthor()),
				book.getTitle(),
				book.getChapterTitle(),
				book.getPublisher() == null ? null : book.getPublisher().getDisplayName(),
				book.getYear(),
				book.getPages(),
				book.getISBN());
	}

	private static String toString(URL url) {
		return joinNonNullNonEmpty(", ",
				asString(url.getAuthor()),
				url.getUniformResourceLocator());
	}

	private static String joinNonNullNonEmpty(String delimiter, Object... objects) {
		return String.join(delimiter, Stream.of(objects)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList()));
	}

	private static String asString(java.util.List<Person> persons) {
		if (persons == null) return "";
		String text = String.join(", ", persons.stream()
				.limit(5)
				.map(ReferenceFactory::compileName)
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
}
