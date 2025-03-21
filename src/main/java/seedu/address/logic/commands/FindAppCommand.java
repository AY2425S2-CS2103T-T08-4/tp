package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.person.Person;

/**
 * Finds applications with matching criteria and displays the corresponding applicants.
 * Keyword matching is case-insensitive.
 */
public class FindAppCommand extends Command {

    public static final String COMMAND_WORD = "findapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all persons who have applications with the specified status "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: st/STATUS\n"
            + "Example: " + COMMAND_WORD + " st/1";

    private final List<Predicate<Application>> predicates;
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
    public FindAppCommand(List<Predicate<Application>> predicates, PredicateCombiner combiner) {
        this.predicates = predicates;
        this.combiner = combiner;
    }

    /**
     * Creates a command with multiple predicates combined with AND logic
     */
    public FindAppCommand(List<Predicate<Application>> predicates) {
        this(predicates, PredicateCombiner.AND);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Create combined predicate for applications
        Predicate<Application> combinedAppPredicate = application -> {
            if (predicates.isEmpty()) {
                return true; // No predicates means no filtering
            }

            boolean result = combiner == PredicateCombiner.AND;
            for (Predicate<Application> predicate : predicates) {
                if (combiner == PredicateCombiner.AND) {
                    result = result && predicate.test(application);
                    if (!result) {
                        return false; // Short-circuit for AND
                    }
                } else { // OR
                    result = result || predicate.test(application);
                    if (result) {
                        return true; // Short-circuit for OR
                    }
                }
            }
            return result;
        };

        // Find all applications that match the criteria
        List<Application> matchingApplications = model.getApplicationList().stream()
                .filter(combinedAppPredicate)
                .toList();

        // Extract the applicants (persons) from matching applications
        Set<Person> matchingPersons = new HashSet<>();
        for (Application app : matchingApplications) {
            matchingPersons.add(app.applicant());
        }

        // Create a predicate for persons that checks if they are in the set of matching applicants
        Predicate<Person> personPredicate = person -> matchingPersons.contains(person);

        // Update the filtered person list in the model
        model.updateFilteredPersonList(personPredicate);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FindAppCommand otherFindAppCommand)) {
            return false;
        }

        return this.predicates.equals(otherFindAppCommand.predicates)
                && this.combiner.equals(otherFindAppCommand.combiner);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicates", this.predicates)
                .add("combiner", this.combiner)
                .toString();
    }
}
