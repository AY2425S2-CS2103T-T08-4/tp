package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose fields match the predicates.
 * Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose fields satisfy "
            + "the specified predicates and displays them as a list with index numbers.\n"
            + "Parameters: [n/NAME_KEYWORDS] [p/PHONE_KEYWORDS] [e/EMAIL_KEYWORDS] [a/ADDRESS_KEYWORDS] [t/TAG]...\n"
            + "Example: " + COMMAND_WORD + " n/alice bob e/gmail.com t/friends";

    private final List<Predicate<Person>> predicates;
    private final PredicateCombiner combiner;

    /**
     * Represents ways to combine multiple predicates
     */
    public enum PredicateCombiner {
        AND, OR
    }

    /**
     * Creates a command with multiple predicates combined with the specified logic
     */
    public FindCommand(List<Predicate<Person>> predicates, PredicateCombiner combiner) {
        this.predicates = predicates;
        this.combiner = combiner;
    }

    /**
     * Creates a command with multiple predicates combined with AND logic
     */
    public FindCommand(List<Predicate<Person>> predicates) {
        this(predicates, PredicateCombiner.AND);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        
        Predicate<Person> combinedPredicate = person -> {
            if (predicates.isEmpty()) {
                return true; // No predicates means no filtering
            }
            
            boolean result = combiner == PredicateCombiner.AND;
            for (Predicate<Person> predicate : predicates) {
                if (combiner == PredicateCombiner.AND) {
                    result = result && predicate.test(person);
                    if (!result) {
                        return false; // Short-circuit for AND
                    }
                } else { // OR
                    result = result || predicate.test(person);
                    if (result) {
                        return true; // Short-circuit for OR
                    }
                }
            }
            return result;
        };
        
        model.updateFilteredPersonList(combinedPredicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }
        
        return this.predicates.equals(otherFindCommand.predicates)
                && this.combiner.equals(otherFindCommand.combiner);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicates", this.predicates)
                .add("combiner", this.combiner)
                .toString();
    }
}
