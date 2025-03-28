package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.AddressBookBuilder;

public class SwitchViewCommandTest {

    private Model model = new ModelManager(new AddressBookBuilder().build(),
            new ApplicationsManager(),
            new UserPrefs());

    @Test
    public void execute_switchesFromPersonToJobView_success() {
        // Set initial view state to person view
        model.setViewState(Model.ViewState.PERSON_VIEW);

        // Execute command
        SwitchViewCommand command = new SwitchViewCommand();
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals("Switched view", result.getFeedbackToUser());
        assertTrue(result.setToggleView());
        assertFalse(result.isClearView());
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_switchesFromJobToPersonView_success() {
        // Set initial view state to job view
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command
        SwitchViewCommand command = new SwitchViewCommand();
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals("Switched view", result.getFeedbackToUser());
        assertTrue(result.setToggleView());
        assertFalse(result.isClearView());
        assertEquals(Model.ViewState.PERSON_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_switchesFromJobDetailToPersonView_success() {
        // Set initial view state to job detail view
        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);

        // Execute command
        SwitchViewCommand command = new SwitchViewCommand();
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals("Switched view", result.getFeedbackToUser());
        assertTrue(result.setToggleView());
        assertFalse(result.isClearView());
        assertEquals(Model.ViewState.PERSON_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_switchesFromPersonDetailToPersonView_success() {
        // Set initial view state to person detail view
        model.setViewState(Model.ViewState.PERSON_DETAIL_VIEW);

        // Execute command
        SwitchViewCommand command = new SwitchViewCommand();
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals("Switched view", result.getFeedbackToUser());
        assertTrue(result.setToggleView());
        assertFalse(result.isClearView());
        assertEquals(Model.ViewState.PERSON_VIEW, model.getCurrentViewState());
    }

    @Test
    public void equals() {
        SwitchViewCommand switchViewCommand1 = new SwitchViewCommand();
        SwitchViewCommand switchViewCommand2 = new SwitchViewCommand();

        // same object -> returns true
        assertTrue(switchViewCommand1.equals(switchViewCommand1));

        // same values -> returns true
        assertTrue(switchViewCommand1.equals(switchViewCommand2));
    }
}
