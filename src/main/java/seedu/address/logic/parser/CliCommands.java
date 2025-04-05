package seedu.address.logic.parser;

import seedu.address.logic.commands.AddApplicationCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.commands.AdvanceApplicationCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteApplicationCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteJobCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditJobCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindAppCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindJobCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListJobCommand;
import seedu.address.logic.commands.SwitchViewCommand;
import seedu.address.logic.commands.ViewJobCommand;
import seedu.address.logic.commands.ViewPersonCommand;

/**
 * Contains Command Line Interface (CLI) command instructions across multiple commands.
 */
public enum CliCommands {
    // Person-specific commands below.
    ADD(AddCommand.COMMAND_WORD, AddCommand.BRIEF_MESSAGE_USAGE),
    DELETE(DeleteCommand.COMMAND_WORD, DeleteCommand.BRIEF_MESSAGE_USAGE),
    EDIT(EditCommand.COMMAND_WORD, EditCommand.BRIEF_MESSAGE_USAGE),
    LIST(ListCommand.COMMAND_WORD, CliCommands.NO_PREFIXES),
    FIND(FindCommand.COMMAND_WORD, FindCommand.BRIEF_MESSAGE_USAGE),
    // Job-specific commands below.
    ADDJOB(AddJobCommand.COMMAND_WORD, AddJobCommand.BRIEF_MESSAGE_USAGE),
    DELETEJOB(DeleteJobCommand.COMMAND_WORD, DeleteJobCommand.BRIEF_MESSAGE_USAGE),
    EDITJOB(EditJobCommand.COMMAND_WORD, EditJobCommand.BRIEF_MESSAGE_USAGE),
    LISTJOB(ListJobCommand.COMMAND_WORD, CliCommands.NO_PREFIXES),
    FINDJOB(FindJobCommand.COMMAND_WORD, FindJobCommand.BRIEF_MESSAGE_USAGE),
    // Application-specific commands below.
    ADDAPP(AddApplicationCommand.COMMAND_WORD, AddApplicationCommand.BRIEF_MESSAGE_USAGE),
    DELETEAPP(DeleteApplicationCommand.COMMAND_WORD, DeleteApplicationCommand.BRIEF_MESSAGE_USAGE),
    ADVANCEAPP(AdvanceApplicationCommand.COMMAND_WORD, AdvanceApplicationCommand.BRIEF_MESSAGE_USAGE),
    FINDAPP(FindAppCommand.COMMAND_WORD, FindAppCommand.BRIEF_MESSAGE_USAGE),
    // View-specific commands below.
    VIEWPERSON(ViewPersonCommand.COMMAND_WORD, ViewPersonCommand.BRIEF_MESSAGE_USAGE),
    VIEWJOB(ViewJobCommand.COMMAND_WORD, ViewJobCommand.BRIEF_MESSAGE_USAGE),
    SWITCHVIEW(SwitchViewCommand.COMMAND_WORD, CliCommands.NO_PREFIXES),
    // Miscellaneous commands below.
    CLEAR(ClearCommand.COMMAND_WORD, CliCommands.NO_PREFIXES),
    HELP(HelpCommand.COMMAND_WORD, CliCommands.NO_PREFIXES),
    EXIT(ExitCommand.COMMAND_WORD, CliCommands.NO_PREFIXES);

    private static final String NO_PREFIXES = "No prefixes needed";

    private final String commandWord;
    private final String messageUsage;

    /**
     * Instantiate a valid command String alongside its command instruction.
     * @param commandWord valid command String parsed.
     * @param messageUsage brief corresponding command instruction.
     */
    CliCommands(String commandWord, String messageUsage) {
        this.commandWord = commandWord;
        this.messageUsage = messageUsage;
    }

    public String getCommandWord() {
        return this.commandWord;
    }

    public String getMessageUsage() {
        return this.messageUsage;
    }

    /**
     * Retrieve the command associated with the potential command word String input, defaulting to null if invalid.
     * @param input potential command word String to validify.
     * @return Corresponding command comprising the command word and its command instruction.
     */
    public static CliCommands fromCommandWord(String input) {
        if (input == null) {
            return null;
        }
        for (CliCommands command : CliCommands.values()) {
            if (command.getCommandWord().equalsIgnoreCase(input.trim())) {
                return command;
            }
        }
        return null;
    }

    public static boolean isValidCommandWord(String input) {
        return fromCommandWord(input) != null;
    }
}
