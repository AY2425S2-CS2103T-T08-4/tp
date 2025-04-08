package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindJobCommand;
import seedu.address.model.job.JobContainsKeywordsPredicate;

public class FindJobCommandParserTest {

    private FindJobCommandParser parser = new FindJobCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindJobCommand() {
        // no leading and trailing whitespaces
        FindJobCommand expectedFindJobCommand =
                new FindJobCommand(new JobContainsKeywordsPredicate(Arrays.asList("Software", "Engineer")));
        assertParseSuccess(parser, "Software Engineer", expectedFindJobCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Software \n \t Engineer  \t", expectedFindJobCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindJobCommand() {
        // Test with single keyword
        FindJobCommand expectedFindJobCommand =
                new FindJobCommand(new JobContainsKeywordsPredicate(Arrays.asList("Software")));
        assertParseSuccess(parser, "Software", expectedFindJobCommand);
    }

    @Test
    public void parse_multipleKeywords_returnsFindJobCommand() {
        // Test with multiple keywords
        FindJobCommand expectedFindJobCommand =
                new FindJobCommand(new JobContainsKeywordsPredicate(
                        Arrays.asList("Software", "Engineer", "Internship")));
        assertParseSuccess(parser, "Software Engineer Internship", expectedFindJobCommand);
    }
}
