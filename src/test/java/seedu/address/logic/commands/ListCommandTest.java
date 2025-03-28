package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        ListCommand listCommand = new ListCommand();
        CommandResult result = listCommand.execute(model);
        assertEquals(CommandResult.withFeedback(ListCommand.MESSAGE_SUCCESS), result);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        ListCommand listCommand = new ListCommand();
        CommandResult result = listCommand.execute(model);
        assertEquals(CommandResult.withFeedback(ListCommand.MESSAGE_SUCCESS), result);
    }
}
