package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindAppCommand;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatusPredicate;

public class FindAppCommandParserTest {

    private FindAppCommandParser parser = new FindAppCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingStatusPrefix_throwsParseException() {
        // Missing st/ prefix
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindAppCommand() {
        // Single status predicate
        List<Predicate<Application>> predicates = new ArrayList<>();
        predicates.add(new ApplicationStatusPredicate(1));
        FindAppCommand expectedFindAppCommand = new FindAppCommand(predicates);
        assertParseSuccess(parser, " st/1", expectedFindAppCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty prefix value
        assertParseFailure(parser, " st/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));

        // Non-integer status
        assertParseFailure(parser, " st/abc", "Status should be a valid integer");
    }
}
