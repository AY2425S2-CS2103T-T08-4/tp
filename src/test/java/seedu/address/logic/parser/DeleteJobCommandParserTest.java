package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_ONE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteJobCommand;

public class DeleteJobCommandParserTest {

    private DeleteJobCommandParser parser = new DeleteJobCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteJobCommand() {
        assertParseSuccess(parser, "1", new DeleteJobCommand(INDEX_ONE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty argument
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));

        // Non-integer argument
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));

        // Negative integer
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));

        // Zero index
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraWhitespace_returnsDeleteJobCommand() {
        // Leading and trailing whitespace
        assertParseSuccess(parser, "  1  ", new DeleteJobCommand(INDEX_ONE));
    }
}
