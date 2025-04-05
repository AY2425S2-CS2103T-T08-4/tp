package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_DSA;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SWE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOB_TITLE_SOFTWARE_ENGINEER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SKILL_JAVA;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showJobAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_ONE;
import static seedu.address.testutil.TypicalIndexes.INDEX_TWO;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditJobCommand.EditJobDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.job.Job;
import seedu.address.testutil.EditJobDescriptorBuilder;
import seedu.address.testutil.JobBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code EditJobCommand}.
 */
public class EditJobCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
        // Set the view state to JOB_VIEW since EditJobCommand can only be executed in job view
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws CommandException {
        Job editedJob = new JobBuilder().build();
        EditJobDescriptor descriptor = new EditJobDescriptorBuilder(editedJob).build();
        EditJobCommand editJobCommand = new EditJobCommand(INDEX_ONE, descriptor);

        String expected = String.format(EditJobCommand.MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob));

        expectedModel.setJob(model.getFilteredJobList().get(0), editedJob);

        CommandResult result = editJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshApplications(expected), result);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws CommandException {
        Index indexLastJob = Index.fromOneBased(model.getFilteredJobList().size());
        Job lastJob = model.getFilteredJobList().get(indexLastJob.getZeroBased());

        JobBuilder jobInList = new JobBuilder(lastJob);
        Job editedJob = jobInList.withJobTitle("AI Engineer").withSkills(VALID_SKILL_JAVA).build();

        EditJobDescriptor descriptor = new EditJobDescriptorBuilder().withJobTitle("AI Engineer")
                .withSkills(VALID_SKILL_JAVA).build();
        EditJobCommand editJobCommand = new EditJobCommand(indexLastJob, descriptor);

        String expected = String.format(EditJobCommand.MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob));

        expectedModel.setJob(lastJob, editedJob);

        CommandResult result = editJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshApplications(expected), result);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws CommandException {
        EditJobCommand editJobCommand = new EditJobCommand(INDEX_ONE, new EditJobDescriptor());
        Job editedJob = model.getFilteredJobList().get(INDEX_ONE.getZeroBased());

        String expected = String.format(EditJobCommand.MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob));

        CommandResult result = editJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshApplications(expected), result);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_filteredList_success() throws CommandException {
        showJobAtIndex(model, INDEX_ONE);
        showJobAtIndex(expectedModel, INDEX_ONE);

        Job jobInFilteredList = model.getFilteredJobList().get(INDEX_ONE.getZeroBased());
        Job editedJob = new JobBuilder(jobInFilteredList).withJobTitle("AI Engineer").build();
        EditJobCommand editJobCommand = new EditJobCommand(INDEX_ONE,
                new EditJobDescriptorBuilder().withJobTitle("AI Engineer").build());

        String expected = String.format(EditJobCommand.MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob));

        expectedModel.setJob(model.getFilteredJobList().get(0), editedJob);
        // reset expectedModel since execute automatically refreshes filters on UniqueJobList
        expectedModel.resetFilteredJobList();

        CommandResult result = editJobCommand.execute(model);

        assertEquals(CommandResult.withRefreshApplications(expected), result);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_duplicateJobUnfilteredList_failure() {
        Job firstJob = model.getFilteredJobList().get(INDEX_ONE.getZeroBased());
        EditJobDescriptor descriptor = new EditJobDescriptorBuilder(firstJob).build();
        EditJobCommand editJobCommand = new EditJobCommand(INDEX_TWO, descriptor);

        assertCommandFailure(editJobCommand, model, EditJobCommand.MESSAGE_DUPLICATE_JOB);
    }

    @Test
    public void execute_duplicateJobFilteredList_failure() {
        showJobAtIndex(model, INDEX_ONE);

        Job jobinList = model.getAddressBook().getJobList().get(INDEX_TWO.getZeroBased());
        EditJobCommand editJobCommand = new EditJobCommand(INDEX_ONE,
                new EditJobDescriptorBuilder(jobinList).build());

        assertCommandFailure(editJobCommand, model, EditJobCommand.MESSAGE_DUPLICATE_JOB);
    }

    @Test
    public void execute_invalidJobIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobList().size() + 1);
        EditJobDescriptor descriptor = new EditJobDescriptorBuilder().withJobTitle(VALID_JOB_TITLE_SOFTWARE_ENGINEER)
                .build();
        EditJobCommand editJobCommand = new EditJobCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editJobCommand, model, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidJobIndexFilteredList_failure() {
        showJobAtIndex(model, INDEX_ONE);
        Index outofBoundIndex = INDEX_TWO;

        assertTrue(outofBoundIndex.getZeroBased() < model.getAddressBook().getJobList().size());

        EditJobCommand editJobCommand = new EditJobCommand(outofBoundIndex,
                new EditJobDescriptorBuilder().withJobTitle(VALID_JOB_TITLE_SOFTWARE_ENGINEER).build());

        assertCommandFailure(editJobCommand, model, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditJobCommand standardCommand = new EditJobCommand(INDEX_TWO, DESC_SWE);

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // same values -> return true
        EditJobDescriptor copyDescriptor = new EditJobDescriptor(DESC_SWE);
        EditJobCommand commandWithSameValues = new EditJobCommand(INDEX_TWO, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditJobCommand(INDEX_ONE, DESC_SWE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditJobCommand(INDEX_TWO, DESC_DSA)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditJobDescriptor editJobDescriptor = new EditJobDescriptor();
        EditJobCommand editJobCommand = new EditJobCommand(index, editJobDescriptor);
        String expected = EditJobCommand.class.getCanonicalName() + "{index=" + index + ", editJobDescriptor="
                + editJobDescriptor + "}";
        assertEquals(expected, editJobCommand.toString());
    }
}
