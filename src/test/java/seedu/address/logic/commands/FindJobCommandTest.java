package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.FindJobCommand.MESSAGE_JOBS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.FindJobCommand.MESSAGE_NO_MATCHES;
import static seedu.address.testutil.TypicalPersons.DATA_SCIENTIST_MICROSOFT;
import static seedu.address.testutil.TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.job.JobContainsKeywordsPredicate;

public class FindJobCommandTest {
    private Model model;
    private Model expectedModel;
    private JobContainsKeywordsPredicate predicate;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
        // Set the view state to JOB_VIEW since EditJobCommand can only be executed in job view
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);
    }

    @Test
    public void execute_zeroKeywords_noJobFound() throws CommandException {
        predicate = preparePredicate(" ");
        FindJobCommand command = new FindJobCommand(predicate);
        expectedModel.updateFilteredJobList(predicate);
        CommandResult expected = CommandResult.withRefreshJobView(MESSAGE_NO_MATCHES);

        CommandResult result = command.execute(model);

        assertEquals(expected, result);
        assertEquals(Collections.emptyList(), model.getFilteredJobList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() throws CommandException {
        predicate = preparePredicate("Data Software");
        FindJobCommand command = new FindJobCommand(predicate);
        expectedModel.updateFilteredJobList(predicate);
        CommandResult expected = CommandResult.withRefreshJobView(String.format(MESSAGE_JOBS_LISTED_OVERVIEW, 2));

        CommandResult result = command.execute(model);

        assertEquals(expected, result);
        assertEquals(Arrays.asList(SOFTWARE_ENGINEER_GOOGLE, DATA_SCIENTIST_MICROSOFT), model.getFilteredJobList());
    }
    @Test
    public void equals() {
        JobContainsKeywordsPredicate firstPredicate = new JobContainsKeywordsPredicate(
                Collections.singletonList("first"));
        JobContainsKeywordsPredicate secondPredicate = new JobContainsKeywordsPredicate(
                Collections.singletonList("second"));

        FindJobCommand findFirstCommand = new FindJobCommand(firstPredicate);
        FindJobCommand findSecondCommand = new FindJobCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindJobCommand findFirstCommandCopy = new FindJobCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(new ListJobCommand()));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different values -> returns falsse
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void toStringMethod() {
        JobContainsKeywordsPredicate predicate = new JobContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindJobCommand findJobCommand = new FindJobCommand(predicate);
        String expected = FindJobCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findJobCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private JobContainsKeywordsPredicate preparePredicate(String userInput) {
        return new JobContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
