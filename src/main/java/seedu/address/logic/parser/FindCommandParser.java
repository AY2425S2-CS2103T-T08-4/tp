package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.tag.TagContainsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, 
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!anyPrefixPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (hasEmptyValue(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<Predicate<Person>> predicates = new ArrayList<>();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            predicates.add(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String[] phoneKeywords = argMultimap.getValue(PREFIX_PHONE).get().split("\\s+");
            predicates.add(new PhoneContainsKeywordsPredicate(Arrays.asList(phoneKeywords)));
        }
        
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String[] emailKeywords = argMultimap.getValue(PREFIX_EMAIL).get().split("\\s+");
            predicates.add(new EmailContainsKeywordsPredicate(Arrays.asList(emailKeywords)));
        }
        
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String[] addressKeywords = argMultimap.getValue(PREFIX_ADDRESS).get().split("\\s+");
            predicates.add(new AddressContainsKeywordsPredicate(Arrays.asList(addressKeywords)));
        }
        
        if (argMultimap.getAllValues(PREFIX_TAG).size() > 0) {
            Collection<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);
            predicates.add(new TagContainsPredicate(tagKeywords));
        }

        return new FindCommand(predicates);
    }

    /**
     * Returns true if any of the prefixes contains a non-empty {@code Optional} value.
     */
    private static boolean anyPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
    
    /**
     * Returns true if any of the prefixes contains an empty value.
     */
    private static boolean hasEmptyValue(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .anyMatch(prefix -> 
                    argumentMultimap.getValue(prefix).isPresent() 
                    && argumentMultimap.getValue(prefix).get().trim().isEmpty());
    }
}
