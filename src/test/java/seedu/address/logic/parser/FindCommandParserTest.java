package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.tag.TagContainsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // Single name predicate
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        FindCommand expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);

        // Multiple predicates
        predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Arrays.asList("Alice")));
        predicates.add(new EmailContainsKeywordsPredicate(Arrays.asList("alice@example.com")));
        expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser, " n/Alice e/alice@example.com", expectedFindCommand);

        // All predicates
        predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Arrays.asList("Alice")));
        predicates.add(new PhoneContainsKeywordsPredicate(Arrays.asList("12345")));
        predicates.add(new EmailContainsKeywordsPredicate(Arrays.asList("alice@example.com")));
        predicates.add(new AddressContainsKeywordsPredicate(Arrays.asList("Main", "Street")));
        List<String> tags = Arrays.asList("friends");
        predicates.add(new TagContainsPredicate(tags));
        expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser, " n/Alice p/12345 e/alice@example.com a/Main Street t/friends",
                expectedFindCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty prefix value
        assertParseFailure(parser, " n/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // Multiple empty prefix values
        assertParseFailure(parser, " n/Alice p/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // Only prefixes, no values
        assertParseFailure(parser, " n/ e/ p/ a/ t/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
