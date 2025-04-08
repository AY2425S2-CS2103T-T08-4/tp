package seedu.address.logic.parser;

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
    public void parse_extraWhitespace_returnsDeleteJobCommand() {
        // Leading and trailing whitespace
        assertParseSuccess(parser, "  1  ", new DeleteJobCommand(INDEX_ONE));
    }
}
