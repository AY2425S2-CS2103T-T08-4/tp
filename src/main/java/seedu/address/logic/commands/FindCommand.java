package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the
 * argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";
    public static final String BRIEF_MESSAGE_USAGE = "KEYWORD [MORE_KEYWORDS]...";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose profile contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE + "\nExample: " + COMMAND_WORD + " alice bob charlie";

    public static final String MESSAGE_NO_PERSONS_FOUND = "No persons found with the specified keywords."
            + " Use the list command to see all persons!";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in person view
        if (model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_PERSON_VIEW);
        }

        model.updateFilteredPersonList(this.predicate);

        int filteredListSize = model.getFilteredPersonList().size();

        String feedbackString = filteredListSize == 0
                ? MESSAGE_NO_PERSONS_FOUND
                : String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, filteredListSize);

        return CommandResult.withFeedback(feedbackString);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }
        return this.predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", this.predicate)
                .toString();
    }
}
