package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

public class AddApplicationCommand extends Command {
    public static final String COMMAND_WORD = "addapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an application to the model manager. "
            + "Parameters: " + PREFIX_NAME + "CANDIDATE'S NAME"
            + PREFIX_JOB_TITLE + "JOB TITLE"
            + PREFIX_JOB_COMPANY + "COMPANY'S NAME"
            + PREFIX_APPLICATION_STATUS + "CURRENT INTERVIEW ROUND";
    public static final String MESSAGE_SUCCESS = "New Application added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPLICATION = "This application already exists in the address book";
    private final Application applicationToAdd;

    /**
     * Creates an AddApplicationCommand to add the specified {@code Application}.
     * @param application A candidate's job application comprising only the most essential identifiable data, including
     *                    candidate's name, job title, company's name, and current interview round by this application.
     */
    public AddApplicationCommand(Application application) {
        requireNonNull(application);
        this.applicationToAdd = application;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasApplication(this.applicationToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPLICATION);
        }
        model.addApplication(this.applicationToAdd); // Crux.
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(this.applicationToAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handle nulls
        if (!(other instanceof AddApplicationCommand otherAddApplicationCommand)) {
            return false;
        }
        return this.applicationToAdd.equals(otherAddApplicationCommand.applicationToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", this.applicationToAdd).toString();
    }
}
