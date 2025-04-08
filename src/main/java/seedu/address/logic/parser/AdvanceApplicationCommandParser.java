package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_INDEX_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AdvanceApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AdvanceApplicationCommand}
 * object.
 */
public class AdvanceApplicationCommandParser implements Parser<AdvanceApplicationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code AdvanceApplicationCommand}
     * and returns an {@code AdvanceApplicationCommand} object for execution.
     *
     * @throws ParseException If the user input does not conform the expected
     *                        format.
     */
    public AdvanceApplicationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);

        // 1st guard condition below: empty job index or application index or trailing
        // whitespace exists before 1st valid prefix
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AdvanceApplicationCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);

        try {
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            Index appByJobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPLICATION_INDEX).get());

            return new AdvanceApplicationCommand(jobIndex, appByJobIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_INDEX_FORMAT,
                    pe.getMessage(), AdvanceApplicationCommand.MESSAGE_USAGE), pe);
        }
    }
}
