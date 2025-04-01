package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AdvanceApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AdvanceApplicationCommand object
 */
public class AdvanceApplicationCommandParser implements Parser<AdvanceApplicationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AdvanceApplicationCommand
     * and returns an AdvanceApplicationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AdvanceApplicationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AdvanceApplicationCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);
        try {
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            Index applicationIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPLICATION_INDEX).get());
            return new AdvanceApplicationCommand(jobIndex, applicationIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AdvanceApplicationCommand.MESSAGE_USAGE), pe);
        } catch (NumberFormatException e) {
            throw new ParseException(
                    String.format("Rounds must be a valid integer.\n%s", AdvanceApplicationCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
