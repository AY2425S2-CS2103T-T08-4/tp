package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_INDEX_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewPersonCommand object
 */
public class ViewPersonCommandParser implements Parser<ViewPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * ViewPersonCommand
     * and returns a ViewPersonCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);

        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewPersonCommand.MESSAGE_USAGE));
        }

        try {
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            Index applicationIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPLICATION_INDEX).get());

            return new ViewPersonCommand(jobIndex, applicationIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_INDEX_FORMAT,
                    pe.getMessage(), ViewPersonCommand.MESSAGE_USAGE), pe);
        }
    }
}
