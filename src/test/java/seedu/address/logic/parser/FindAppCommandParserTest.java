package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_ONE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindAppCommand;
import seedu.address.model.application.ApplicationStatus;

public class FindAppCommandParserTest {

    private FindAppCommandParser parser = new FindAppCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingCompulsoryField_throwsParseException() {
        // Missing application status
        assertParseFailure(parser, " " + PREFIX_JOB_INDEX + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        // Non-empty preamble
        assertParseFailure(parser, "some random string " + PREFIX_APPLICATION_STATUS + "2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_validArgsWithoutJobIndex_returnsFindAppCommand() {
        // Valid application status without job index
        ApplicationStatus status = new ApplicationStatus(2);
        FindAppCommand expectedCommand = new FindAppCommand(status);
        assertParseSuccess(parser, " " + PREFIX_APPLICATION_STATUS + "2", expectedCommand);
    }

    @Test
    public void parse_validArgsWithJobIndex_returnsFindAppCommand() {
        // Valid application status with job index
        ApplicationStatus status = new ApplicationStatus(2);
        Index jobIndex = INDEX_ONE;
        FindAppCommand expectedCommand = new FindAppCommand(jobIndex, status);
        assertParseSuccess(parser,
                " " + PREFIX_APPLICATION_STATUS + "2 " + PREFIX_JOB_INDEX + "1",
                expectedCommand);
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsFindAppCommand() {
        // Multiple whitespaces
        ApplicationStatus status = new ApplicationStatus(2);
        FindAppCommand expectedCommand = new FindAppCommand(status);
        assertParseSuccess(parser, "  " + PREFIX_APPLICATION_STATUS + "2  ", expectedCommand);

        // Multiple whitespaces with job index
        Index jobIndex = INDEX_ONE;
        FindAppCommand expectedCommandWithJobIndex = new FindAppCommand(jobIndex, status);
        assertParseSuccess(parser,
                "  " + PREFIX_APPLICATION_STATUS + "2  " + PREFIX_JOB_INDEX + "1  ",
                expectedCommandWithJobIndex);
    }
}
