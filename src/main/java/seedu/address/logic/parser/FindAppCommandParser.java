package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindAppCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindAppCommand object
 */
public class FindAppCommandParser implements Parser<FindAppCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindAppCommand
     * and returns a FindAppCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindAppCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_JOB_INDEX, PREFIX_APPLICATION_STATUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_APPLICATION_STATUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
        }

        String rounds = argMultimap.getValue(PREFIX_APPLICATION_STATUS).get();

        // If job index is present, parse it
        if (arePrefixesPresent(argMultimap, PREFIX_JOB_INDEX)) {
            try {
                Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
                return new FindAppCommand(jobIndex, rounds);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE), pe);
            }
        }

        // If no job index, just return command with rounds
        return new FindAppCommand(rounds);
    }
}
