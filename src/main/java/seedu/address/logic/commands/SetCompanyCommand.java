package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Sets and updates Company information.
 */
public class SetCompanyCommand extends Command {
    public static final String COMMAND_WORD = "set";

    @Override
    public CommandResult execute(Model model) {
        return CommandResult.withFeedback("Updated company information!");
    }
}
