package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;

/**
 * Adds a Job to the address book.
 */
public class AddJobCommand extends Command {
    public static final String COMMAND_WORD = "addjob";
    public static final String BRIEF_MESSAGE_USAGE = PREFIX_JOB_TITLE + "JOB_TITLE "
            + PREFIX_JOB_ROUNDS + "NUMBER_OF_ROUNDS (must be between 1 and "
            + JobRounds.MAX_ROUNDS + ") [" + PREFIX_SKILL + "SKILL]";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the model manager.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "\nExample: " + COMMAND_WORD + " " + PREFIX_JOB_TITLE + "Software Engineering "
            + PREFIX_JOB_ROUNDS + "3 [" + PREFIX_SKILL + "Python]";
    public static final String MESSAGE_SUCCESS = "New job added: %1$s";
    public static final String MESSAGE_DUPLICATE_JOB = "This job already exists in the address book";

    private final Job toAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}
     */
    public AddJobCommand(Job job) {
        requireNonNull(job);
        toAdd = job;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // 1st guard condition below: Person view is not intended for adding a new job.
        if (!model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_JOB_VIEW);
        }
        // 2nd guard condition below: Preexisting valid job. Code below traces to Job::isSameJob.
        if (model.hasJob(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOB);
        }

        model.addJob(toAdd);
        return CommandResult.withRefreshJobViewOnly(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AddJobCommand otherAddJobCommand)) {
            return false;
        }
        return this.toAdd.equals(otherAddJobCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("job", toAdd)
                .toString();
    }
}
