package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindByNameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindByNameCommand object
 */
public class FindCommandParser implements Parser<FindByNameCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindByNameCommand
     * and returns a FindByNameCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindByNameCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByNameCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindByNameCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
