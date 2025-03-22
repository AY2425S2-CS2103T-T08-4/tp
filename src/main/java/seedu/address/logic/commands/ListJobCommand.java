package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
        model.resetFilteredJobList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
