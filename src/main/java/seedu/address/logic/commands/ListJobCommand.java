package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_JOBS;

import seedu.address.model.Model;

/**
 * Lists all jobs in the address book to the user.
 */
public class ListJobCommand extends Command {

    public static final String COMMAND_WORD = "listjob";

    public static final String MESSAGE_SUCCESS = "Listed all jobs";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        return CommandResult.withFeedback(MESSAGE_SUCCESS);
    }
}
