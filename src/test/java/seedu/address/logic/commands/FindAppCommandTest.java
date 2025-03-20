package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatusPredicate;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) for {@code FindAppCommand}.
 */
public class FindAppCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), 
            new UserPrefs());

    @Test
    public void equals() {
        // Create two different predicate lists
        List<Predicate<Application>> firstPredicateList = new ArrayList<>();
        firstPredicateList.add(new ApplicationStatusPredicate(1));

        List<Predicate<Application>> secondPredicateList = new ArrayList<>();
        secondPredicateList.add(new ApplicationStatusPredicate(2));

        FindAppCommand findFirstCommand = new FindAppCommand(firstPredicateList);
        FindAppCommand findSecondCommand = new FindAppCommand(secondPredicateList);

        // Same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // Same values -> returns true
        FindAppCommand findFirstCommandCopy = new FindAppCommand(firstPredicateList);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // Different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // Null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // Different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // Different combiner -> returns false
        FindAppCommand findFirstCommandAnd = new FindAppCommand(firstPredicateList, FindAppCommand.PredicateCombiner.AND);
        FindAppCommand findFirstCommandOr = new FindAppCommand(firstPredicateList, FindAppCommand.PredicateCombiner.OR);
        assertFalse(findFirstCommandAnd.equals(findFirstCommandOr));
    }

    @Test
    public void execute_singlePredicate_multiplePersonsFound() {
        // Filter for applications with status 1, which should return CARL, ELLE, and ALICE (for her Microsoft application)
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        ApplicationStatusPredicate predicate = new ApplicationStatusPredicate(1);
        FindAppCommand command = new FindAppCommand(Collections.singletonList(predicate));

        // Getting persons with status 1 applications
        Set<Person> matchingPersons = getPersonsWithApplicationStatus(1);
        Predicate<Person> expectedPredicate = person -> matchingPersons.contains(person);
        
        expectedModel.updateFilteredPersonList(expectedPredicate);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, CARL, ELLE));
    }

    @Test
    public void execute_singlePredicate_singlePersonFound() {
        // Filter for applications with status 4, which should only return BENSON
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        ApplicationStatusPredicate predicate = new ApplicationStatusPredicate(4);
        FindAppCommand command = new FindAppCommand(Collections.singletonList(predicate));

        // Getting persons with status 4 applications
        Set<Person> matchingPersons = getPersonsWithApplicationStatus(4);
        Predicate<Person> expectedPredicate = person -> matchingPersons.contains(person);
        
        expectedModel.updateFilteredPersonList(expectedPredicate);
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(BENSON));
    }

    @Test
    public void execute_singlePredicate_noPersonFound() {
        // Filter for applications with status 5, which shouldn't match any applications
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        ApplicationStatusPredicate predicate = new ApplicationStatusPredicate(5);
        FindAppCommand command = new FindAppCommand(Collections.singletonList(predicate));

        expectedModel.updateFilteredPersonList(p -> false); // No matches
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multiplePredicatesWithOr_multiplePersonsFound() {
        // Filter for applications with status 1 or 2, which should return multiple persons
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 4);
        
        List<Predicate<Application>> predicates = new ArrayList<>();
        predicates.add(new ApplicationStatusPredicate(1));
        predicates.add(new ApplicationStatusPredicate(2));
        
        FindAppCommand command = new FindAppCommand(predicates, FindAppCommand.PredicateCombiner.OR);

        // Getting persons with status 1 or 2 applications
        Set<Person> matchingPersons = new HashSet<>();
        matchingPersons.addAll(getPersonsWithApplicationStatus(1));
        matchingPersons.addAll(getPersonsWithApplicationStatus(2));
        
        Predicate<Person> expectedPredicate = person -> matchingPersons.contains(person);
        
        expectedModel.updateFilteredPersonList(expectedPredicate);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, CARL, DANIEL, ELLE));
    }

    /**
     * Returns a set of persons who have applications with the specified status.
     */
    private Set<Person> getPersonsWithApplicationStatus(int status) {
        Set<Person> persons = new HashSet<>();
        for (Application app : model.getApplicationList()) {
            if (app.applicationStatus().applicationStatus == status) {
                persons.add(app.applicant());
            }
        }
        return persons;
    }

    /**
     * Asserts that {@code command} is successfully executed, and:
     * - the command feedback is equal to {@code expectedMessage}
     * - the {@code FilteredList<Person>} is equal to {@code expectedList}
     */
    private void assertCommandSuccess(FindAppCommand command, String expectedMessage, List<Person> expectedList) {
        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedList, model.getFilteredPersonList());
    }
} 