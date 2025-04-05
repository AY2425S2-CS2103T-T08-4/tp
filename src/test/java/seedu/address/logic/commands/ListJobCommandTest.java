package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.showJobAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_ONE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * ListJobCommand.
 */
public class ListJobCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());

        // Set the view state to JOB_VIEW since ListCommand can only be executed in job view
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() throws CommandException {
        // Verify starting in JOB_VIEW
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());

        ListJobCommand listJobCommand = new ListJobCommand();
        CommandResult result = listJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshJobView(ListJobCommand.MESSAGE_SUCCESS), result);
        assertEquals(expectedModel, model);

        // Verify still in PERSON_VIEW after command execution
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_listIsFiltered_showsEverything() throws CommandException {
        // Verify starting in JOB_VIEW
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());

        showJobAtIndex(model, INDEX_ONE);
        ListJobCommand listJobCommand = new ListJobCommand();
        CommandResult result = listJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshJobView(ListJobCommand.MESSAGE_SUCCESS), result);

        // Verify still in JOB_VIEW after command execution
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());
    }
}
