package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showJobAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_ONE;
import static seedu.address.testutil.TypicalIndexes.INDEX_TWO;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.job.Job;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteJobCommand}.
 */
public class DeleteJobCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);
    }
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Job jobtoDelete = model.getFilteredJobList().get(INDEX_ONE.getZeroBased());
        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(INDEX_ONE);

        String expectedMessage = String.format(DeleteJobCommand.MESSAGE_DELETE_JOB_SUCCESS,
                Messages.format(jobtoDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(),
                new ApplicationsManager(model.getApplicationsManager()), new UserPrefs());
        expectedModel.deleteJob(jobtoDelete);

        assertCommandSuccess(deleteJobCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobList().size() + 1);
        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(outOfBoundIndex);

        assertCommandFailure(deleteJobCommand, model, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showJobAtIndex(model, INDEX_ONE);
        showJobAtIndex(expectedModel, INDEX_ONE);

        Job jobToDelete = model.getFilteredJobList().get(INDEX_ONE.getZeroBased());
        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(INDEX_ONE);

        String expected = String.format(DeleteJobCommand.MESSAGE_DELETE_JOB_SUCCESS, Messages.format(jobToDelete));

        expectedModel.deleteJob(jobToDelete);

        assertCommandSuccess(deleteJobCommand, model, expected, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showJobAtIndex(model, INDEX_ONE);

        Index outOfBoundIndex = INDEX_TWO;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getJobList().size());

        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(outOfBoundIndex);

        assertCommandFailure(deleteJobCommand, model, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);

    }

    @Test
    public void equals() {
        DeleteJobCommand deleteJobFirstCommand = new DeleteJobCommand(INDEX_ONE);
        DeleteJobCommand deleteJobSecondCommand = new DeleteJobCommand(INDEX_TWO);

        // same object -> returns true
        assertTrue(deleteJobFirstCommand.equals(deleteJobFirstCommand));

        // same values -> returns true
        DeleteJobCommand deleteJobFirstCommandCopy = new DeleteJobCommand(INDEX_ONE);
        assertTrue(deleteJobFirstCommand.equals(deleteJobFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteJobFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteJobFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteJobFirstCommand.equals(deleteJobSecondCommand));
    }
}
