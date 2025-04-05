package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;

import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Adds an {@code application} to the address book. {@code AddApplicationCommand} needs only single dispatch as
 * {@code applicationByJobIndex} doesn't exist for new applications.
 */
public class AddApplicationCommand extends Command {
    public static final String COMMAND_WORD = "addapp";
    public static final String BRIEF_MESSAGE_USAGE = PREFIX_PERSON_INDEX + "PERSON INDEX IN PERSON VIEW "
            + PREFIX_JOB_INDEX + "JOB INDEX IN JOB VIEW";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an application to the address book.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "\nExample: " + COMMAND_WORD + " " + PREFIX_PERSON_INDEX + "1 " + PREFIX_JOB_INDEX + "2";
    public static final String MESSAGE_SUCCESS = "New application added as follows:\nApplication added: {%1$s}";
    public static final String MESSAGE_DUPLICATE_APPLICATION = "This application already exists in the address book "
            + "Try using " + DeleteApplicationCommand.COMMAND_WORD + " instead!";
    public static final String MESSAGE_INVALID_PERSON = "This application's person does not exist in the address book";
    public static final String MESSAGE_INVALID_JOB = "This application's job does not exist in the address book";

    private final Index personIndex;
    private final Index jobIndex;
    private Application applicationToAdd = null;

    /**
     * Creates an {@code AddApplicationCommand} to add the specified {@code application}.
     */
    public AddApplicationCommand(Index personIndex, Index jobIndex) {
        requireAllNonNull(personIndex, jobIndex);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> lastShownPersonList = model.getFilteredPersonList();
        ObservableList<Job> lastShownJobList = model.getFilteredJobList();

        // 1st guard condition below: Invalid person index.
        if (this.personIndex.getZeroBased() >= lastShownPersonList.size() || this.personIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }
        Person matchingPerson = lastShownPersonList.get(personIndex.getZeroBased());

        // 2nd guard condition below: Invalid job index.
        if (this.jobIndex.getZeroBased() >= lastShownJobList.size() || this.jobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_JOB);
        }
        Job matchingJob = lastShownJobList.get(this.jobIndex.getZeroBased());

        // 3rd guard condition below: Duplicate existing application.

        Optional<Application> matchingApplication = model.getApplicationByPersonAndJob(matchingPerson, matchingJob);

        if (matchingApplication.isPresent()) {
            throw new CommandException(MESSAGE_DUPLICATE_APPLICATION);
        }

        // Finally apply main logic of adding new valid application to model.
        this.applicationToAdd = new Application(matchingPerson, matchingJob, new ApplicationStatus(0));
        model.addApplication(this.applicationToAdd);

        String successMessage = String.format(MESSAGE_SUCCESS, this.applicationToAdd);

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
        return this.personIndex.equals(otherAddApplicationCommand.personIndex)
                && this.jobIndex.equals(otherAddApplicationCommand.jobIndex)
                && (this.applicationToAdd != null)
                ? this.applicationToAdd.equals(otherAddApplicationCommand.applicationToAdd)
                : (this.applicationToAdd == null && otherAddApplicationCommand.applicationToAdd == null);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Application added", this.applicationToAdd).toString();
    }
}
