package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Switches the view between person and job.
 */
public class SwitchViewCommand extends Command {
    public static final String COMMAND_WORD = "switchview";

    @Override
    public CommandResult execute(Model model) {
        // Toggle the model's view state
        model.toggleJobView();

        return CommandResult.withToggleView("Switched view");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls. Simplify if-else as all SwitchViewCommand instances lack state thus they are equal.
        return other instanceof SwitchViewCommand;
    }
}
