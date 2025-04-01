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
 * Advances the {@code ApplicationStatus} of an {@code Application} by 1 round.
 */
public class AdvanceApplicationCommand extends Command {
    public static final String COMMAND_WORD = "advapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Advances an application in the model manager.\n"
            + "Parameters: " + PREFIX_JOB_INDEX + " JOB INDEX IN JOB VIEW "
            + PREFIX_APPLICATION_INDEX + " APPLICATION INDEX BY JOB IN JOB VIEW\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_APPLICATION_INDEX + "2";
    public static final String MESSAGE_SUCCESS =
            "Application advanced as follows:\nInitial application advanced: {%1$s}\nNumber of rounds advanced: 1";
    public static final String MESSAGE_EXCEED_ROUNDS = "This application is already at the last round and cannot be "
            + "advanced in the address book. Try using DeleteApplicationCommand to delete the application instead!";
    public static final String MESSAGE_INVALID_JOB = "This job does not exist in the address book. "
            + "Try using AddJobCommand to add a job first, then use AddApplicationCommand to add an application!";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using AddApplicationCommand to add an application first!";

    private final Index jobIndex;
    private final Index applicationByJobIndex;
    private Application applicationToAdvance = null;

    /**
     * Creates an AdvanceApplicationCommand to advance the application for the specified job
     * at the specified application index by the specified number of rounds.
     */
    public AdvanceApplicationCommand(Index jobIndex, Index applicationByJobIndex) {
        requireAllNonNull(jobIndex, applicationByJobIndex);
        requireNonNull(applicationByJobIndex);
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
        Job matchingJob = lastShownJobList.get(jobIndex.getZeroBased());
        // Get applications for this job.
        List<Application> jobApplications = model.getFilteredApplicationsByJob(matchingJob);
        // 2nd guard condition below: Invalid application by job index.
        if (this.applicationByJobIndex.getZeroBased() >= jobApplications.size()
                || this.applicationByJobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_APPLICATION);
        }
        this.applicationToAdvance = jobApplications.get(this.applicationByJobIndex.getZeroBased());
        // 3rd guard condition below: Application is at its final round.
        if (this.applicationToAdvance.getApplicationStatus().applicationStatus
                >= this.applicationToAdvance.getJob().getJobRounds().jobRounds) {
            throw new CommandException(MESSAGE_EXCEED_ROUNDS);
        }
        // Apply main logic of advancing valid existing application in model.
        this.applicationToAdvance = model.advanceApplication(this.applicationToAdvance, 1);
        String successMessage = String.format(MESSAGE_SUCCESS, this.applicationToAdvance);
        // Return a CommandResult that signals applications need to be refreshed
        return CommandResult.withRefreshApplications(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AdvanceApplicationCommand otherAdvanceApplicationCommand)) {
            return false;
        }
        return this.jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                && this.applicationByJobIndex.equals(otherAdvanceApplicationCommand.applicationByJobIndex)
                && this.applicationToAdvance == otherAdvanceApplicationCommand.applicationToAdvance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Initial application advanced", this.applicationToAdvance)
                .toString();
    }
}
