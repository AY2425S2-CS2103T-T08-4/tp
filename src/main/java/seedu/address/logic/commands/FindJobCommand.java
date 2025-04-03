package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.JobContainsKeywordsPredicate;

/**
 * Finds and lists all jobs in address book whose JobTitle contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindJobCommand extends Command {

    public static final String COMMAND_WORD = "findjob";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all Jobs' whose profile contains any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " Software Engineering";

    public static final String MESSAGE_WRONG_VIEW = "This command is only available in job view. "
            + "Please switch to job view first using 'switchview' command.";

    private final JobContainsKeywordsPredicate predicate;

    public FindJobCommand(JobContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }

        model.updateFilteredJobList(predicate);

        // Clear the detail view if we don't find any results
        if (model.getFilteredJobList().isEmpty()) {
            // Reset the view state to job view (default view)
            model.setViewState(Model.ViewState.JOB_VIEW);
            return CommandResult.withRefreshJobView(
                String.format(Messages.MESSAGE_JOBS_LISTED_OVERVIEW, 0));
        } else {
            return CommandResult.withRefreshJobView(
                String.format(Messages.MESSAGE_JOBS_LISTED_OVERVIEW, model.getFilteredJobList().size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindJobCommand otherFindJobCommand)) {
            return false;
        }
        return this.predicate.equals(otherFindJobCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", this.predicate)
                .toString();
    }
}
