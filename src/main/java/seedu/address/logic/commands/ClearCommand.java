package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {
    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been permanently cleared!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        model.setApplicationsManager(new ApplicationsManager());
        return CommandResult.withClearView(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls. Simplify if-else as all ClearCommand instances lack
        // state thus they are equal.
        return other instanceof ClearCommand;
    }
}
