package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.application.ApplicationStatusPredicate;
import seedu.address.model.job.Job;

/**
 * Filters the list to show only applications with the specified status.
 * Can only be used in job view.
 */
public class FindAppCommand extends Command {

    public static final String COMMAND_WORD = "findapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters the list to show only applications "
            + "with the specified status in job view.\n"
            + "Parameters: " + PREFIX_APPLICATION_STATUS + "ROUNDS "
            + "[" + PREFIX_JOB_INDEX + "JOB_INDEX]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPLICATION_STATUS + " 2 " + PREFIX_JOB_INDEX + " 1 ";
    public static final String MESSAGE_SUCCESS = "Filtered applications by status: %1$s";
    public static final String MESSAGE_NO_MATCHES = "No applications found with status: %1$s";
    public static final String MESSAGE_JOB_NOT_FOUND = "The specified job index is invalid";
    public static final String MESSAGE_WRONG_VIEW = "This command is only available in job view. "
            + "Please switch to job view first using 'switchview' command.";

    private static final Logger logger = LogsCenter.getLogger(FindAppCommand.class);

    private final String status;
    private final Optional<Index> jobIndex;

    /**
     * Creates a FindAppCommand to filter by the specified status
     */
    public FindAppCommand(String status) {
        this.status = status;
        this.jobIndex = Optional.empty();
    }

    /**
     * Creates a FindAppCommand to filter by the specified job and status
     */
    public FindAppCommand(Index jobIndex, String status) {
        this.status = status;
        this.jobIndex = Optional.of(jobIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        logger.info("Executing FindAppCommand with status: " + status + ", jobIndex: " + jobIndex);
        logger.info("Current view state: " + model.getCurrentViewState());

        // Check if we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }

        // Check if we need to reset the view
        if (model.getCurrentViewState() == Model.ViewState.JOB_DETAIL_VIEW
                || model.getCurrentViewState() == Model.ViewState.PERSON_DETAIL_VIEW) {
            model.setViewState(Model.ViewState.JOB_VIEW);
            logger.info("Reset to JOB_VIEW from detail view");
        }

        // Create the status predicate
        ApplicationStatus targetStatus = new ApplicationStatus(Integer.parseInt(status));
        ApplicationStatusPredicate statusPredicate = new ApplicationStatusPredicate(targetStatus);

        if (jobIndex.isPresent()) {
            // If job index is specified, first filter the job list
            List<Job> lastShownList = model.getFilteredJobList();
            if (jobIndex.get().getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_JOB_NOT_FOUND);
            }

            Job jobToFilter = lastShownList.get(jobIndex.get().getZeroBased());
            logger.info("Filtering for job: " + jobToFilter.getJobTitle().jobTitle());

            // Update the job list to only show this job
            model.updateFilteredJobList(job -> job.equals(jobToFilter));

            // Get all applications for this job
            List<Application> jobApplications = model.getApplicationsByJob(jobToFilter);

            // Filter these applications by the status predicate
            List<Application> filteredJobApps = jobApplications.stream()
                    .filter(statusPredicate::test)
                    .toList();

            // Update application list to show only these filtered applications
            model.updateFilteredApplicationList(filteredJobApps::contains);
        } else {
            // No job index specified, filter all applications by status first
            // Get the jobs that have applications with the specified status
            List<Job> jobsWithMatchingApplications = model.getFilteredJobList().stream()
                    .filter(job -> {
                        List<Application> jobApps = model.getApplicationsByJob(job);
                        return jobApps.stream().anyMatch(statusPredicate);
                    })
                    .toList();

            // Update job list to show only jobs with matching applications
            model.updateFilteredJobList(jobsWithMatchingApplications::contains);

            // Update application list to show only applications with the specified status
            model.updateFilteredApplicationList(statusPredicate);
        }

        // Check if there are any results after filtering
        boolean hasResults = !model.getFilteredApplicationList().isEmpty();

        if (!hasResults) {
            // When no applications match the filter status, we should show no jobs at all
            // This will make the job panel appear empty with "no jobs found" message

            // Update job list to an empty list by using a predicate that matches nothing
            model.updateFilteredJobList(job -> false);

            // Still use withRefreshJobView to ensure the UI updates correctly
            return CommandResult.withRefreshJobView(String.format(MESSAGE_NO_MATCHES, status));
        }

        // Use withRefreshJobView to ensure both clearView and refreshJobView are set to
        // true
        // This ensures that the general statistics panel is shown and all charts are
        // updated
        return CommandResult.withRefreshJobView(String.format(MESSAGE_SUCCESS, status));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof FindAppCommand otherCommand)) {
            return false;
        }
        return this.status.equals(otherCommand.status)
                && this.jobIndex.equals(otherCommand.jobIndex);
    }
}
