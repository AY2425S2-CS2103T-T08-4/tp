package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEGREE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";
    public static final String BRIEF_MESSAGE_USAGE =
            PREFIX_NAME + "NAME " + PREFIX_PHONE + "PHONE_NUMBER "
            + PREFIX_EMAIL + "EMAIL " + PREFIX_ADDRESS + "HOME_ADDRESS "
            + PREFIX_SCHOOL + "SCHOOL " + PREFIX_DEGREE + "DEGREE " + "[" + PREFIX_SKILL + "SKILL]...";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "\nExample: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe " + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com " + PREFIX_ADDRESS + "444, Jurong West Ave 1, #12-082 "
            + PREFIX_SCHOOL + "NUS " + PREFIX_DEGREE + "CS " + PREFIX_SKILL + "Java " + PREFIX_SKILL + "PYTHON";
    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in person view
        if (model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_PERSON_VIEW);
        }

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return CommandResult.withFeedback(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }
        return this.toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
