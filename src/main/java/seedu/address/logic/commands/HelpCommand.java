package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliCommands;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display by default, or a specific help instruction.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = "Displays all command words and formats by default";
    public static final String MESSAGE_SUCCESS = "Help window opened.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // Reduced to single dispatch below. CliCommands enum enables future multiple dispatch if necessary.
        return CommandResult.withHelp(MESSAGE_SUCCESS);
    }

    public static String getGeneralHelpMessage() {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        for (CliCommands command : CliCommands.values()) {
            if (command.equals(CliCommands.ADD)) {
                sb.append("Person-specific Commands below:\n");
            } else if (command.equals(CliCommands.ADDJOB)) {
                sb.append("\nJob-specific Commands below:\n");
            } else if (command.equals(CliCommands.ADDAPP)) {
                sb.append("\nApplication-specific Commands below:\n");
            } else if (command.equals(CliCommands.VIEWPERSON)) {
                sb.append("\nView-toggling Commands below:\n");
            } else if (command.equals(CliCommands.CLEAR)) {
                sb.append("\nMiscellaneous Commands below:\n");
            }
            sb.append(command.getCommandWord()).append(": ").append(command.getMessageUsage()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls. Simplify if-else as all HelpCommand instances lack state thus they are equal.
        return other instanceof HelpCommand;
    }
}
