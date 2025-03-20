package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindAppCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatusPredicate;

/**
 * Parses input arguments and creates a new FindAppCommand object
 */
public class FindAppCommandParser implements Parser<FindAppCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindAppCommand
     * and returns a FindAppCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindAppCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS);

        // Status must be provided
        if (!argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
        }

        // Check if status value is empty
        String statusValue = argMultimap.getValue(PREFIX_STATUS).get();
        if (statusValue.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
        }

        List<Predicate<Application>> predicates = new ArrayList<>();

        try {
            int status = Integer.parseInt(statusValue);
            predicates.add(new ApplicationStatusPredicate(status));
        } catch (NumberFormatException e) {
            throw new ParseException("Status should be a valid integer");
        } catch (IllegalArgumentException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAppCommand.MESSAGE_USAGE));
        }

        return new FindAppCommand(predicates);
    }
}
