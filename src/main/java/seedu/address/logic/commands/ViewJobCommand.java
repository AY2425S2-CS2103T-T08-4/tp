package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Displays detailed information about a specific job.
 */
public class ViewJobCommand extends Command {

    public static final String COMMAND_WORD = "viewjob";

    public static final String MESSAGE_SUCCESS = "Viewing job: %s";
    public static final String MESSAGE_INVALID_JOB_INDEX = "The job index provided is invalid";
    public static final String MESSAGE_NOT_IN_JOB_VIEW = "Please switch to job view first using 'switchview' command.";
    public static final String MESSAGE_NO_APPLICATIONS = "This job has no applications yet.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views the detailed information of a job. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    public ViewJobCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        // Get the job at the specified index
        Job job = lastShownList.get(targetIndex.getZeroBased());
        int jobIndex = targetIndex.getZeroBased();

        // Check if there are any applications for this job
        List<Application> jobApplications = model.getApplicationsByJob(job);
        if (jobApplications.isEmpty()) {
            throw new CommandException(MESSAGE_NO_APPLICATIONS);
        }

        // Update the model state to JOB_DETAIL_VIEW
        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);

        return CommandResult.withJobView(
                String.format(MESSAGE_SUCCESS, job.getJobTitle().jobTitle()),
                jobIndex);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewJobCommand // instanceof handles nulls
                && targetIndex.equals(((ViewJobCommand) other).targetIndex)); // state check
    }
}
