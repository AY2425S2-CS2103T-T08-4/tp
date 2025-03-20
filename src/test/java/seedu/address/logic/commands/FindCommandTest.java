package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new ApplicationsManager(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new ApplicationsManager(), new UserPrefs());

    @Test
    public void equals() {
        // Create two different predicate lists
        List<Predicate<Person>> firstPredicateList = new ArrayList<>();
        firstPredicateList.add(new NameContainsKeywordsPredicate(Collections.singletonList("first")));

        List<Predicate<Person>> secondPredicateList = new ArrayList<>();
        secondPredicateList.add(new NameContainsKeywordsPredicate(Collections.singletonList("second")));

        FindCommand findFirstCommand = new FindCommand(firstPredicateList);
        FindCommand findSecondCommand = new FindCommand(secondPredicateList);

        // Same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // Same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicateList);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // Different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // Null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // Different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // Different combiner -> returns false
        FindCommand findFirstCommandAnd = new FindCommand(firstPredicateList, FindCommand.PredicateCombiner.AND);
        FindCommand findFirstCommandOr = new FindCommand(firstPredicateList, FindCommand.PredicateCombiner.OR);
        assertFalse(findFirstCommandAnd.equals(findFirstCommandOr));
    }

    @Test
    public void execute_singlePredicate_multiplePersonsFound() {
        // Single name predicate
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        FindCommand command = new FindCommand(Collections.singletonList(predicate));
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(ALICE));
    }

    @Test
    public void execute_multiplePredicatesWithAnd_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);

        // Create two predicates that only ELLE should match
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList("Elle")));
        predicates.add(new PhoneContainsKeywordsPredicate(Collections.singletonList("9482224")));

        FindCommand command = new FindCommand(predicates, FindCommand.PredicateCombiner.AND);

        // Create the combined predicate for the expected model
        Predicate<Person> combinedPredicate = person ->
                predicates.get(0).test(person) && predicates.get(1).test(person);

        expectedModel.updateFilteredPersonList(combinedPredicate);
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(ELLE));
    }

    @Test
    public void execute_multiplePredicatesWithOr_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);

        // Create two predicates that should match ALICE and CARL
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList("Alice")));
        predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList("Carl")));

        FindCommand command = new FindCommand(predicates, FindCommand.PredicateCombiner.OR);

        // Create the combined predicate for the expected model
        Predicate<Person> combinedPredicate = person ->
                predicates.get(0).test(person) || predicates.get(1).test(person);

        expectedModel.updateFilteredPersonList(combinedPredicate);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, CARL));
    }

    @Test
    public void execute_differentFieldPredicates_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);

        // Create predicates for different fields that should match only ALICE
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList("Alice")));
        predicates.add(new AddressContainsKeywordsPredicate(Collections.singletonList("Jurong")));

        FindCommand command = new FindCommand(predicates, FindCommand.PredicateCombiner.AND);

        // Create the combined predicate for the expected model
        Predicate<Person> combinedPredicate = person ->
                predicates.get(0).test(person) && predicates.get(1).test(person);

        expectedModel.updateFilteredPersonList(combinedPredicate);
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(ALICE));
    }

    @Test
    public void execute_noMatchingPredicates_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        // Create predicates that won't match any person
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList("Nobody")));

        FindCommand command = new FindCommand(predicates);

        expectedModel.updateFilteredPersonList(p -> false); // No matches
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    /**
     * Asserts that {@code command} is successfully executed, and:
     * - the command feedback is equal to {@code expectedMessage}
     * - the {@code FilteredList<Person>} is equal to {@code expectedList}
     */
    private void assertCommandSuccess(FindCommand command, String expectedMessage, List<Person> expectedList) {
        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedList, model.getFilteredPersonList());
    }
}
