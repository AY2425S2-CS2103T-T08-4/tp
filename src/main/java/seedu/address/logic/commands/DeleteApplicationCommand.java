package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Deletes an {@code Application} from the address book.
 */
public class DeleteApplicationCommand extends Command {
    public static final String COMMAND_WORD = "delapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an application from the model manager. "
            + "Parameters: " + PREFIX_JOB_INDEX + " JOB INDEX IN JOB VIEW "
            + PREFIX_APPLICATION_INDEX + " APPLICATION INDEX BY JOB IN JOB VIEW\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_APPLICATION_INDEX + "2";
    public static final String MESSAGE_SUCCESS = "Deleted application as follows:\nApplication deleted: {%1$s}";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using AddApplicationCommand to add an application first!";
    public static final String MESSAGE_INVALID_JOB = "This application's job does not exist in the address book.";

    private final Index jobIndex;
    private final Index applicationByJobIndex;
    private Application applicationToDelete = null;

    /**
     * Creates a {@code DeleteApplicationCommand} to delete the {@code application} associated with the person and job
     * by toggling between person view and job view.
     */
    public DeleteApplicationCommand(Index jobIndex, Index applicationByJobIndex) {
        requireAllNonNull(jobIndex, applicationByJobIndex);
        this.jobIndex = jobIndex;
        this.applicationByJobIndex = applicationByJobIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Job> lastShownJobList = model.getFilteredJobList();
        // 1st guard condition below: Invalid job index.
        if (this.jobIndex.getZeroBased() >= lastShownJobList.size() || this.jobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_JOB);
        }
        Job matchingJob = lastShownJobList.get(this.jobIndex.getZeroBased());
        // Get applications for this job
        List<Application> jobApplications = model.getFilteredApplicationsByJob(matchingJob);
        // 2nd guard condition below: Invalid application by job index.
        if (this.applicationByJobIndex.getZeroBased() >= jobApplications.size()
                || this.applicationByJobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_APPLICATION);
        }
        this.applicationToDelete = jobApplications.get(this.applicationByJobIndex.getZeroBased());
        // Apply main logic of deleting valid existing application from model in-place.
        model.deleteApplication(this.applicationToDelete);
        String successMessage = String.format(MESSAGE_SUCCESS, this.applicationToDelete);
        // Return a CommandResult that signals applications need to be refreshed
        return CommandResult.withRefreshApplications(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof DeleteApplicationCommand otherDeleteApplicationCommand)) {
            return false;
        }
        return this.jobIndex.equals(otherDeleteApplicationCommand.jobIndex)
                && this.applicationByJobIndex.equals(otherDeleteApplicationCommand.applicationByJobIndex)
                && this.applicationToDelete.equals(otherDeleteApplicationCommand.applicationToDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Application deleted", this.applicationToDelete)
                .toString();
    }
}
