package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_INDEX_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewJobCommand object
 */
public class ViewJobCommandParser implements Parser<ViewJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewJobCommand
     * and returns a ViewJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewJobCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewJobCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_INDEX_FORMAT,
                    pe.getMessage(), ViewJobCommand.MESSAGE_USAGE), pe);
        }
    }
}
