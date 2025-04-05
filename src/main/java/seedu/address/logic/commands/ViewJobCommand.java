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
    public static final String BRIEF_MESSAGE_USAGE = "INDEX (must be a positive integer)";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views the detailed information of a job. "
            + "Parameters: " + BRIEF_MESSAGE_USAGE + "\nExample: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Viewing job: %s";
    public static final String MESSAGE_SUCCESS_NO_APPLICATIONS = "Viewing job: %s (No applications yet)";

    private final Index targetIndex;

    public ViewJobCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_JOB_VIEW);
        }

        List<Job> lastShownList = model.getFilteredJobList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        // Get the job at the specified index
        Job job = lastShownList.get(targetIndex.getZeroBased());
        int jobIndex = targetIndex.getZeroBased();

        // Check if there are any applications for this job
        List<Application> jobApplications = model.getFilteredApplicationsByJob(job);

        // Update the model state to JOB_DETAIL_VIEW
        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);

        String resultMessage;
        if (jobApplications.isEmpty()) {
            resultMessage = String.format(MESSAGE_SUCCESS_NO_APPLICATIONS, job.getJobTitle().jobTitle());
        } else {
            resultMessage = String.format(MESSAGE_SUCCESS, job.getJobTitle().jobTitle());
        }

        return CommandResult.withJobView(resultMessage, jobIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewJobCommand otherViewJobCommand)) {
            return false;
        }
        return this.targetIndex.equals(otherViewJobCommand.targetIndex); // state check
    }
}
