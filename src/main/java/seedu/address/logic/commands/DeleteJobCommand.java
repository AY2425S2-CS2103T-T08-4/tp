package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;

/**
 * Deletes a Job from the address book.
 */
public class DeleteJobCommand extends Command {
    public static final String COMMAND_WORD = "deletejob";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the job identified by the index number used in the displayed job list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_JOB_SUCESS = "Deleted Job: %1$s";

    public static final String MESSAGE_INVALID_JOB = "This Job does not exist in the address book";
    private final Index targetIndex;

    public DeleteJobCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToDelete = lastShownList.get(targetIndex.getZeroBased());

        if (!model.hasJob(jobToDelete)) {
            throw new CommandException(MESSAGE_INVALID_JOB);
        }

        model.deleteJob(jobToDelete);
        return CommandResult.withFeedback(String.format(MESSAGE_DELETE_JOB_SUCESS, Messages.format(jobToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DeleteJobCommand otherDeleteJobCommand)) {
            return false;
        }
        return this.targetIndex.equals(otherDeleteJobCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Job: ", this.targetIndex)
                .toString();
    }

}
