package seedu.address.model.tag;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Tag}s match any of the keywords given.
 */
public class TagContainsPredicate implements Predicate<Person> {
    private final Collection<String> tagKeywords;

    public TagContainsPredicate(Collection<String> tagKeywords) {
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        // Extract all tag names from the person
        Collection<String> personTags = person.getTags().stream()
                .map(tag -> tag.tagName())
                .collect(Collectors.toList());
        // Check if any of the required tag keywords match any of the person's tags
        return tagKeywords.stream()
                .anyMatch(keyword -> personTags.stream()
                        .anyMatch(tagName -> tagName.equalsIgnoreCase(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagContainsPredicate)) {
            return false;
        }

        TagContainsPredicate otherTagContainsPredicate = (TagContainsPredicate) other;
        return tagKeywords.equals(otherTagContainsPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tagKeywords", tagKeywords).toString();
    }
}
