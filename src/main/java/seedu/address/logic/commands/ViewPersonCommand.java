package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Shows detailed information about a person from a specific job.
 */
public class ViewPersonCommand extends Command {
    public static final String COMMAND_WORD = "viewperson";
    public static final String BRIEF_MESSAGE_USAGE =
            PREFIX_JOB_INDEX + "JOB_INDEX " + PREFIX_APPLICATION_INDEX + "APPLICATION_INDEX";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views detailed information for the person identified by the "
            + "job index and application index within that job.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "\nExample: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_APPLICATION_INDEX + "2";
    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Viewing Person: %1$s (from Job: %2$s)";
    public static final String MESSAGE_NO_SUCH_APPLICATION = "No application with "
            + "index %1$d found for job with index %2$d.";

    private final Index jobIndex;
    private final Index applicationIndex;

    /**
     * @param jobIndex Index of the job in the filtered job list
     * @param applicationIndex Index of the application for that job
     */
    public ViewPersonCommand(Index jobIndex, Index applicationIndex) {
        requireNonNull(jobIndex);
        requireNonNull(applicationIndex);
        this.jobIndex = jobIndex;
        this.applicationIndex = applicationIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_JOB_VIEW);
        }

        List<Job> jobs = model.getFilteredJobList();

        // Validate the job index
        if (jobIndex.getZeroBased() >= jobs.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job job = jobs.get(jobIndex.getZeroBased());

        // Get applications for this job
        List<Application> jobApplications = model.getFilteredApplicationsByJob(job);

        // Validate the application index within the job's applications
        if (applicationIndex.getZeroBased() >= jobApplications.size()) {
            throw new CommandException(String.format(MESSAGE_NO_SUCH_APPLICATION,
                    applicationIndex.getOneBased(), jobIndex.getOneBased()));
        }

        // Get the application using the application index directly
        Application application = jobApplications.get(applicationIndex.getZeroBased());
        Person person = application.getApplicant();

        // Set the view state to PERSON_DETAIL_VIEW before returning the CommandResult
        model.setViewState(Model.ViewState.PERSON_DETAIL_VIEW);

        return CommandResult.withPersonView(
                String.format(MESSAGE_VIEW_PERSON_SUCCESS, person.getName().fullName, job.getJobTitle().jobTitle()),
                jobIndex.getZeroBased(),
                applicationIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof ViewPersonCommand otherViewPersonCommand)) {
            return false;
        }
        return this.jobIndex.equals(otherViewPersonCommand.jobIndex)
                && this.applicationIndex.equals(otherViewPersonCommand.applicationIndex);
    }
}
