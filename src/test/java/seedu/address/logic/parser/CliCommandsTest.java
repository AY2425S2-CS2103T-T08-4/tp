package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.DeleteCommand;

public class CliCommandsTest {
    @Test
    void testGetCommandWord() {
        assertEquals("add", CliCommands.ADD.getCommandWord());
        assertNotEquals("ADD", CliCommands.ADD.getCommandWord());
        assertEquals("del", CliCommands.DELETE.getCommandWord());
        assertEquals("help", CliCommands.HELP.getCommandWord());
    }

    @Test
    void testGetMessageUsage() {
        assertEquals(AddCommand.BRIEF_MESSAGE_USAGE, CliCommands.ADD.getMessageUsage());
        assertEquals(DeleteCommand.BRIEF_MESSAGE_USAGE, CliCommands.DELETE.getMessageUsage());
        assertEquals("No prefixes needed", CliCommands.HELP.getMessageUsage()); // Uses NO_PREFIXES
    }

    @Test
    void parse_validCommands_success() {
        assertEquals(CliCommands.ADD, CliCommands.fromCommandWord("add"));
        assertEquals(CliCommands.ADD, CliCommands.fromCommandWord("ADD")); // Case-insensitive
        assertEquals(CliCommands.DELETE, CliCommands.fromCommandWord(" del ")); // Trimmed input
        assertEquals(CliCommands.LISTJOB, CliCommands.fromCommandWord("listjob"));
    }

    @Test
    void parse_invalidCommands_nullValue() {
        assertNull(CliCommands.fromCommandWord("randomcommand")); // Invalid command
        assertNull(CliCommands.fromCommandWord("")); // Empty string
        assertNull(CliCommands.fromCommandWord("   ")); // Whitespaces
        assertNull(CliCommands.fromCommandWord(null)); // Null input
    }

    @Test
    void testIsValidCommandWord() {
        assertTrue(CliCommands.isValidCommandWord("add"));
        assertTrue(CliCommands.isValidCommandWord("ADD"));
        assertFalse(CliCommands.isValidCommandWord("notacommand"));
        assertFalse(CliCommands.isValidCommandWord(""));
        assertFalse(CliCommands.isValidCommandWord(null));
    }
}
