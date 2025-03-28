package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Deletes an application between a person and a job.
 */
public class DeleteApplicationCommand extends Command {

    public static final String COMMAND_WORD = "deleteapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the application between the specified person and job. "
            + "Parameters: "
            + PREFIX_PERSON_INDEX + "PERSON_INDEX "
            + PREFIX_JOB_INDEX + "JOB_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSON_INDEX + "1 "
            + PREFIX_JOB_INDEX + "2";

    public static final String MESSAGE_DELETE_APPLICATION_SUCCESS =
            "Deleted application: %1$s's application for %2$s";
    public static final String MESSAGE_APPLICATION_NOT_FOUND =
            "No application found for this person and job";
    public static final String MESSAGE_PERSON_INVALID_INDEX =
            "Person index is invalid";
    public static final String MESSAGE_JOB_INVALID_INDEX =
            "Job index is invalid";

    private final Index personIndex;
    private final Index jobIndex;

    /**
     * Creates a DeleteApplicationCommand to delete the application associated with the specified person and job.
     */
    public DeleteApplicationCommand(Index personIndex, Index jobIndex) {
        requireNonNull(personIndex);
        requireNonNull(jobIndex);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Job> lastShownJobList = model.getFilteredJobList();

        if (personIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(MESSAGE_PERSON_INVALID_INDEX);
        }

        if (jobIndex.getZeroBased() >= lastShownJobList.size()) {
            throw new CommandException(MESSAGE_JOB_INVALID_INDEX);
        }

        Person person = lastShownPersonList.get(personIndex.getZeroBased());
        Job job = lastShownJobList.get(jobIndex.getZeroBased());

        // Find the application for this person and job
        List<Application> personApplications = model.getApplicationsByPerson(person);
        Application targetApplication = null;

        for (Application app : personApplications) {
            if (app.getJob().equals(job)) {
                targetApplication = app;
                break;
            }
        }

        if (targetApplication == null) {
            throw new CommandException(MESSAGE_APPLICATION_NOT_FOUND);
        }

        model.deleteApplication(targetApplication);

        String successMessage = String.format(MESSAGE_DELETE_APPLICATION_SUCCESS,
                person.getName(), job.getJobTitle());

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

        return personIndex.equals(otherDeleteApplicationCommand.personIndex)
                && jobIndex.equals(otherDeleteApplicationCommand.jobIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("jobIndex", jobIndex)
                .toString();
    }
}
