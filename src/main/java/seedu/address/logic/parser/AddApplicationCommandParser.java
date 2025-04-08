package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_INDEX_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddApplicationCommand object
 */
public class AddApplicationCommandParser implements Parser<AddApplicationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code AddApplicationCommand}
     * and returns an {@code AddApplicationCommand} object for execution.
     *
     * @throws ParseException If the user input does not conform the expected
     *                        format.
     */
    public AddApplicationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX);
        if (!arePrefixesPresent(argMultimap, PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddApplicationCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX);
        try {
            Index personIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PERSON_INDEX).get());
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            return new AddApplicationCommand(personIndex, jobIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_INDEX_FORMAT,
                    pe.getMessage(), AddApplicationCommand.MESSAGE_USAGE), pe);
        }
    }
}
