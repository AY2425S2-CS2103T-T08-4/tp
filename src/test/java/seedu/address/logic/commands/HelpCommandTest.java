package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.CliCommands;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private final Model model = new ModelManager();
    private final Model expectedModel = new ModelManager();

    @Test
    public void execute_helpCommand_success() {
        CommandResult expectedCommandResult = CommandResult.withHelp(HelpCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(new HelpCommand(), this.model, expectedCommandResult, this.expectedModel);
    }

    @Test
    public void getGeneralHelpMessage_containsAllExpectedSectionsAndCommands() {
        String helpMessage = HelpCommand.getGeneralHelpMessage();
        assertTrue(helpMessage.contains("Person-specific Commands below:"));
        assertTrue(helpMessage.contains("Job-specific Commands below:"));
        assertTrue(helpMessage.contains("Application-specific Commands below:"));
        assertTrue(helpMessage.contains("View-toggling Commands below:"));
        assertTrue(helpMessage.contains("Miscellaneous Commands below:"));

        assertTrue(helpMessage.contains(CliCommands.ADD.getCommandWord()));
        assertTrue(helpMessage.contains(CliCommands.ADD.getMessageUsage()));
        assertTrue(helpMessage.contains(CliCommands.VIEWJOB.getCommandWord()));
    }

    @Test
    public void equals() {
        HelpCommand helpCommand = new HelpCommand();
        // Same object type -> true. Different object type -> false.
        assertEquals(helpCommand, helpCommand);
        assertEquals(new HelpCommand(), helpCommand);
        assertNotEquals(null, helpCommand);
        assertNotEquals(new Object(), helpCommand);
    }
}
