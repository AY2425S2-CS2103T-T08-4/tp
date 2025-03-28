package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROUNDS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Advances an application by a specified number of rounds.
 */
public class AdvanceApplicationCommand extends Command {

    public static final String COMMAND_WORD = "advanceapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Advances an application by the specified number of rounds. "
            + "Parameters: "
            + PREFIX_PERSON_INDEX + "PERSON_INDEX "
            + PREFIX_JOB_INDEX + "JOB_INDEX "
            + PREFIX_ROUNDS + "ROUNDS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSON_INDEX + "1 "
            + PREFIX_JOB_INDEX + "2 "
            + PREFIX_ROUNDS + "1";

    public static final String MESSAGE_ADVANCE_APPLICATION_SUCCESS =
            "%1$s has completed %3$d/%4$d rounds for %2$s";
    public static final String MESSAGE_EXCEED_ROUNDS =
            "Cannot advance application beyond the maximum number of rounds for this job";
    public static final String MESSAGE_INVALID_ROUNDS =
            "Number of rounds must be a positive integer";
    public static final String MESSAGE_APPLICATION_NOT_FOUND =
            "No application found for this person and job";
    public static final String MESSAGE_PERSON_INVALID_INDEX =
            "Person index is invalid";
    public static final String MESSAGE_JOB_INVALID_INDEX =
            "Job index is invalid";

    private final Index personIndex;
    private final Index jobIndex;
    private final int rounds;

    /**
     * Creates an AdvanceApplicationCommand to advance the application for the specified person and job
     * by the specified number of rounds.
     */
    public AdvanceApplicationCommand(Index personIndex, Index jobIndex, int rounds) {
        requireNonNull(personIndex);
        requireNonNull(jobIndex);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
        this.rounds = rounds;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (rounds <= 0) {
            throw new CommandException(MESSAGE_INVALID_ROUNDS);
        }

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

        try {
            Application advancedApplication = model.advanceApplication(targetApplication, rounds);

            String successMessage = String.format(MESSAGE_ADVANCE_APPLICATION_SUCCESS,
                    person.getName(), job.getJobTitle(),
                    advancedApplication.getApplicationStatus().applicationStatus,
                    job.getJobRounds().jobRounds);

            // Return a CommandResult that signals applications need to be refreshed
            return CommandResult.withRefreshApplications(successMessage);
        } catch (InvalidApplicationStatusException e) {
            throw new CommandException(MESSAGE_EXCEED_ROUNDS);
        } catch (IllegalArgumentException e) {
            throw new CommandException(MESSAGE_INVALID_ROUNDS);
        }
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

        return personIndex.equals(otherAdvanceApplicationCommand.personIndex)
                && jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                && rounds == otherAdvanceApplicationCommand.rounds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("jobIndex", jobIndex)
                .add("rounds", rounds)
                .toString();
    }
}
