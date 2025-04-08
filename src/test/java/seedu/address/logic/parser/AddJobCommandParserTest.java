package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddJobCommand;
import seedu.address.model.job.JobRounds;

class AddJobCommandParserTest {

    private AddJobCommandParser parser = new AddJobCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE);

        // missing job title prefix
        assertParseFailure(parser, "Software Engineer " + PREFIX_JOB_ROUNDS + "3", expectedMessage);

        // missing job rounds prefix
        assertParseFailure(parser, " " + PREFIX_JOB_TITLE + "Software Engineer 3", expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, "Software Engineer 3", expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid job rounds
        assertParseFailure(parser, " " + PREFIX_JOB_TITLE + "Software Engineer " + PREFIX_JOB_ROUNDS + "abc",
                JobRounds.MESSAGE_CONSTRAINTS);

        // invalid job rounds (negative)
        assertParseFailure(parser, " " + PREFIX_JOB_TITLE + "Software Engineer " + PREFIX_JOB_ROUNDS + "-1",
                JobRounds.MESSAGE_CONSTRAINTS);

        // invalid job rounds (zero)
        assertParseFailure(parser, " " + PREFIX_JOB_TITLE + "Software Engineer " + PREFIX_JOB_ROUNDS + "0",
                JobRounds.MESSAGE_CONSTRAINTS);

        // invalid job rounds (too large)
        assertParseFailure(parser, " " + PREFIX_JOB_TITLE + "Software Engineer " + PREFIX_JOB_ROUNDS + "100",
                JobRounds.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE);

        // non-empty preamble
        assertParseFailure(parser, " preamble " + PREFIX_JOB_TITLE + "Software Engineer " + PREFIX_JOB_ROUNDS + "3",
                expectedMessage);
    }
}
