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
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Adds an application with a person applying for a job.
 */
public class AddApplicationCommand extends Command {

    public static final String COMMAND_WORD = "addapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a new application with a person applying for a job. "
            + "Parameters: "
            + PREFIX_PERSON_INDEX + "PERSON_INDEX "
            + PREFIX_JOB_INDEX + "JOB_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSON_INDEX + "1 "
            + PREFIX_JOB_INDEX + "2";

    public static final String MESSAGE_SUCCESS = "New application created: "
            + "%1$s has applied for %2$s (0 rounds completed)";
    public static final String MESSAGE_DUPLICATE_APPLICATION = "This application already exists";
    public static final String MESSAGE_PERSON_INVALID_INDEX = "Person index is invalid";
    public static final String MESSAGE_JOB_INVALID_INDEX = "Job index is invalid";

    private final Index personIndex;
    private final Index jobIndex;

    /**
     * Creates an AddApplicationCommand to add an application for the person at the specified index
     * to the job at the specified index.
     */
    public AddApplicationCommand(Index personIndex, Index jobIndex) {
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

        Person personToApply = lastShownPersonList.get(personIndex.getZeroBased());
        Job jobToApplyFor = lastShownJobList.get(jobIndex.getZeroBased());

        // Check if an application with the same person and job already exists (regardless of status)
        List<Application> existingPersonApplications = model.getApplicationsByPerson(personToApply);
        for (Application existingApp : existingPersonApplications) {
            if (existingApp.getJob().equals(jobToApplyFor)) {
                throw new CommandException(MESSAGE_DUPLICATE_APPLICATION);
            }
        }

        // Create a new application with initial status of 0 (applied but no rounds completed)
        Application application = new Application(personToApply, jobToApplyFor, new ApplicationStatus(0));

        if (model.hasApplication(application)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPLICATION);
        }

        model.addApplication(application);

        String successMessage = String.format(MESSAGE_SUCCESS,
                personToApply.getName(), jobToApplyFor.getJobTitle());

        // Return a CommandResult that signals applications need to be refreshed
        return CommandResult.withRefreshApplications(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddApplicationCommand otherAddApplicationCommand)) {
            return false;
        }

        return personIndex.equals(otherAddApplicationCommand.personIndex)
                && jobIndex.equals(otherAddApplicationCommand.jobIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("jobIndex", jobIndex)
                .toString();
    }
}
